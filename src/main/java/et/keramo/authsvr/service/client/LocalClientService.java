package et.keramo.authsvr.service.client;

import et.keramo.authsvr.repository.rdb.local.client.Client;
import et.keramo.authsvr.repository.rdb.local.client.ClientRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("localClientService")
public class LocalClientService {

    private final PasswordEncoder encoder;
    private final TokenStore tokenStore;
    private final ClientRepository repository;

    public List<ClientDto> list() {
        return this.repository.findAll().stream().map(ClientDto::new).collect(Collectors.toList());
    }

    public ClientDto get(String clientId) {
        return this.repository.findById(clientId).map(ClientDto::new).orElse(null);
    }

    public ClientDto add(ClientDto clientDto) throws Exception {
        if (clientDto.getClientId() == null) {
            throw new Exception("클라이언트 ID를 입력해주세요.");
        }

        boolean isExist = this.repository.findById(clientDto.getClientId()).isPresent();
        if (isExist) {
            throw new Exception("이미 존재하는 클라이언트입니다.");
        }

        Key key = MacProvider.generateKey(SignatureAlgorithm.HS256);
        String keyStr = Base64.getEncoder().encodeToString(key.getEncoded());
        clientDto.setSecret(this.encoder.encode(keyStr));

        Client savedClient = this.repository.saveAndFlush(clientDto.toEntity());

        ClientDto savedClientDto = new ClientDto(savedClient);
        savedClientDto.setSecret(keyStr);

        return savedClientDto;
    }

    public String updateSecret(String clientId, String secret) throws Exception {
        Client client = this.checkClient(clientId);

        if (this.encoder.matches(secret, client.getSecret())) {
            Key key = MacProvider.generateKey(SignatureAlgorithm.HS256);
            String keyStr = Base64.getEncoder().encodeToString(key.getEncoded());
            ClientDto clientDto = new ClientDto(client);
            clientDto.setSecret(this.encoder.encode(keyStr));

            this.repository.saveAndFlush(clientDto.toEntity());

            return keyStr;
        }

        throw new Exception("클라이언트 시크릿 키가 올바르지 않습니다.");
    }

    public boolean updateScope(String clientId, String scope) throws Exception {
        Client client = this.checkClient(clientId);

        ClientDto clientDto = new ClientDto(client);
        clientDto.setSecret(client.getSecret());
        clientDto.setScope(scope.replaceAll(" ", ""));

        this.repository.saveAndFlush(clientDto.toEntity());

        return true;
    }

    public boolean updateGrantTypes(String clientId, String grantTypes) throws Exception {
        Client client = this.checkClient(clientId);

        ClientDto clientDto = new ClientDto(client);
        clientDto.setSecret(client.getSecret());
        clientDto.setAuthorizedGrantTypes(grantTypes.replaceAll(" ", ""));

        this.repository.saveAndFlush(clientDto.toEntity());

        return true;
    }

    public boolean updateValidity(String clientId, Integer access, Integer refresh) throws Exception {
        Client client = this.checkClient(clientId);

        ClientDto clientDto = new ClientDto(client);
        clientDto.setSecret(client.getSecret());
        clientDto.setAccessTokenValidity(access);

        if (refresh != null) {
            clientDto.setRefreshTokenValidity(refresh);
        }

        this.repository.saveAndFlush(clientDto.toEntity());

        return true;
    }

    public boolean delete(String clientId) {
        this.repository.deleteById(clientId);
        this.tokenStore.findTokensByClientId(clientId).forEach(accessToken -> {
            if (accessToken.getRefreshToken() != null) {
                this.tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }
            this.tokenStore.removeAccessToken(accessToken);
        });
        return true;
    }

    private Client checkClient(String clientId) throws Exception {
        Client client = this.repository.findById(clientId).orElse(null);

        if (client == null) {
            throw new Exception("존재하지 않는 클라이언트입니다.");
        }
        return client;
    }

}
