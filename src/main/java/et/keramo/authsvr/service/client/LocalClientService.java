package et.keramo.authsvr.service.client;

import et.keramo.authsvr.repository.client.LocalClient;
import et.keramo.authsvr.repository.client.LocalClientRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static io.jsonwebtoken.lang.Assert.*;

@RequiredArgsConstructor
public class LocalClientService implements ClientService<LocalClientDto> {

    private final PasswordEncoder encoder;
    private final TokenStore tokenStore;
    private final LocalClientRepository repository;

    @Override
    public List<LocalClientDto> list() {
        return this.repository.findAll().stream().map(LocalClientDto::new).collect(Collectors.toList());
    }

    @Override
    public LocalClientDto get(String clientId) {
        return this.repository.findById(clientId).map(LocalClientDto::new).orElse(null);
    }

    @Override
    public LocalClientDto add(LocalClientDto clientDto) throws Exception {
//        LocalClientDto clientDto = new ObjectMapper().convertValue(client, LocalClientDto.class);

        boolean isExist = this.repository.findById(clientDto.getClientId()).isPresent();
        if (isExist) {
            throw new Exception("이미 존재하는 클라이언트입니다.");
        }
        return saveWithNewSecret(clientDto);
    }

    @Override
    public LocalClientDto update(LocalClientDto clientDto) throws Exception {
//        Client client = this.checkClient(clientId);
//
//        LocalClientDto clientDto = new LocalClientDto(client);
//        clientDto.setSecret(client.getSecret());
//        clientDto.setScope(scope.replaceAll(" ", ""));
//
//        Client savedClient = this.repository.saveAndFlush(clientDto.toEntity());
//
//        return new LocalClientDto(savedClient);
        return null;
    }

    @Override
    public LocalClientDto updateSecret(String clientId) throws Exception {
        LocalClient client = this.checkClient(clientId);
        return saveWithNewSecret(new LocalClientDto(client));
    }

    @Override
    public LocalClientDto updateGrantTypes(String clientId, String[] grantTypes) throws Exception {
        LocalClient client = this.checkClient(clientId);

        LocalClientDto clientDto = new LocalClientDto(client);
        clientDto.setSecret(client.getSecret());
        clientDto.setGrantTypes(grantTypes);

        LocalClient savedClient = this.repository.saveAndFlush(clientDto.toEntity());

        return new LocalClientDto(savedClient);
    }

    @Override
    public LocalClientDto updateValidity(String clientId, Integer access, Integer refresh) throws Exception {
        LocalClient client = this.checkClient(clientId);

        LocalClientDto clientDto = new LocalClientDto(client);
        clientDto.setSecret(client.getSecret());
        clientDto.setAccessTokenValidity(access == null ? client.getAccessTokenValidity() : access);
        clientDto.setRefreshTokenValidity(refresh == null ? client.getRefreshTokenValidity() : refresh);

        LocalClient savedClient = this.repository.saveAndFlush(clientDto.toEntity());

        return new LocalClientDto(savedClient);
    }

    @Override
    public void delete(String clientId) {
        this.repository.deleteById(clientId);

        this.tokenStore.findTokensByClientId(clientId).forEach(accessToken -> {
            if (accessToken.getRefreshToken() != null) {
                this.tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }
            this.tokenStore.removeAccessToken(accessToken);
        });
    }


    private LocalClientDto saveWithNewSecret(LocalClientDto clientDto) {
        String key = Base64.getEncoder().encodeToString(MacProvider.generateKey(SignatureAlgorithm.HS256).getEncoded());
        clientDto.setSecret(this.encoder.encode(key));

        LocalClient savedClient = this.repository.saveAndFlush(clientDto.toEntity());

        LocalClientDto savedClientDto = new LocalClientDto(savedClient);
        savedClientDto.setSecret(key);

        return savedClientDto;
    }

    private LocalClient checkClient(String clientId) throws Exception {
        return this.repository.findById(clientId).orElseThrow(() -> new Exception("존재하지 않는 클라이언트입니다."));
    }

}
