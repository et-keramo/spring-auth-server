-- 사용자인증정보
CREATE TABLE "oauth_user"
(
	"user_id"  CHARACTER VARYING(50)       NOT NULL, -- 사용자ID
	"password" CHARACTER VARYING(200)      NOT NULL, -- 비밀번호
	"roles"    CHARACTER VARYING(255)      NULL,     -- 역할
	"reg_dt"   TIMESTAMP WITHOUT TIME ZONE NOT NULL, -- 등록일시
	"mod_dt"   TIMESTAMP WITHOUT TIME ZONE NULL      -- 수정일시
)
WITH (
OIDS=false
);

-- 사용자인증정보 기본키
CREATE UNIQUE INDEX "PK_oauth_user"
	ON "oauth_user"
	( -- 사용자인증정보
		"user_id" ASC -- 사용자ID
	)
;
-- 사용자인증정보
ALTER TABLE "oauth_user"
	ADD CONSTRAINT "PK_oauth_user"
		 -- 사용자인증정보 기본키
	PRIMARY KEY
	USING INDEX "PK_oauth_user";

-- 사용자인증정보 기본키
COMMENT ON CONSTRAINT "PK_oauth_user" ON "oauth_user" IS '사용자인증정보 기본키';


-- 클라이언트정보
CREATE TABLE "oauth_client"
(
	"client_id"               CHARACTER VARYING(256)  NOT NULL, -- 클라이언트ID
	"client_secret"           CHARACTER VARYING(256)  NOT NULL, -- 클라이언트시크릿
	"resource_ids"            CHARACTER VARYING(256)  NULL,     -- 리소스ID
	"scope"                   CHARACTER VARYING(256)  NOT NULL, -- 접근범위
	"authorized_grant_types"  CHARACTER VARYING(256)  NOT NULL, -- 인증방식
	"web_server_redirect_uri" CHARACTER VARYING(256)  NULL,     -- 인증결과수신URI
	"authorities"             CHARACTER VARYING(256)  NULL,     -- 클라이언트권한
	"access_token_validity"   BIGINT                  NULL     DEFAULT 1800, -- 액세스토큰유효시간
	"refresh_token_validity"  BIGINT                  NULL     DEFAULT 10800, -- 리프레시토큰유효시간
	"additional_information"  CHARACTER VARYING(4096) NULL,     -- 추가정보
	"autoapprove"             CHARACTER VARYING(256)  NULL      -- Approval화면표출여부
)
WITH (
OIDS=false
);

-- 클라이언트정보 기본키
CREATE UNIQUE INDEX "PK_oauth_client"
	ON "oauth_client"
	( -- 클라이언트정보
		"client_id" ASC -- 클라이언트ID
	)
;
-- 클라이언트정보
ALTER TABLE "oauth_client"
	ADD CONSTRAINT "PK_oauth_client"
		 -- 클라이언트정보 기본키
	PRIMARY KEY
	USING INDEX "PK_oauth_client";

-- 클라이언트정보 기본키
COMMENT ON CONSTRAINT "PK_oauth_client" ON "oauth_client" IS '클라이언트정보 기본키';


-- 클라이언트토큰정보
CREATE TABLE "oauth_client_token"
(
	"authentication_id" CHARACTER VARYING(256) NOT NULL, -- 인증ID
	"token_id"          CHARACTER VARYING(256) NULL,     -- 토큰ID
	"token"             BYTEA                   NULL,     -- 토큰
	"user_name"         CHARACTER VARYING(256) NULL,     -- 사용자명
	"client_id"         CHARACTER VARYING(256) NULL      -- 클라이언트ID
)
WITH (
OIDS=false
);

-- 클라이언트토큰정보 기본키
CREATE UNIQUE INDEX "PK_oauth_client_token"
	ON "oauth_client_token"
	( -- 클라이언트토큰정보
		"authentication_id" ASC -- 인증ID
	)
;
-- 클라이언트토큰정보
ALTER TABLE "oauth_client_token"
	ADD CONSTRAINT "PK_oauth_client_token"
		 -- 클라이언트토큰정보 기본키
	PRIMARY KEY
	USING INDEX "PK_oauth_client_token";

-- 클라이언트토큰정보 기본키
COMMENT ON CONSTRAINT "PK_oauth_client_token" ON "oauth_client_token" IS '클라이언트토큰정보 기본키';


-- 액세스토큰정보
CREATE TABLE "oauth_access_token"
(
	"authentication_id" CHARACTER VARYING(256) NOT NULL, -- 인증ID
	"authentication"    BYTEA                   NULL,     -- 인증
	"token_id"          CHARACTER VARYING(256) NULL,     -- 토큰ID
	"token"             BYTEA                   NULL,     -- 토큰
	"refresh_token"     CHARACTER VARYING(256) NULL,     -- 리프레시토큰
	"user_name"         CHARACTER VARYING(256) NULL,     -- 사용자명
	"client_id"         CHARACTER VARYING(256) NULL      -- 클라이언트ID
)
WITH (
OIDS=false
);

-- 액세스토큰정보 기본키
CREATE UNIQUE INDEX "PK_oauth_access_token"
	ON "oauth_access_token"
	( -- 액세스토큰정보
		"authentication_id" ASC -- 인증ID
	)
;
-- 액세스토큰정보
ALTER TABLE "oauth_access_token"
	ADD CONSTRAINT "PK_oauth_access_token"
		 -- 액세스토큰정보 기본키
	PRIMARY KEY
	USING INDEX "PK_oauth_access_token";

-- 액세스토큰정보 기본키
COMMENT ON CONSTRAINT "PK_oauth_access_token" ON "oauth_access_token" IS '액세스토큰정보 기본키';


-- 리프레시토큰정보
CREATE TABLE "oauth_refresh_token"
(
	"token_id"       CHARACTER VARYING(256) NULL, -- 토큰ID
	"token"          BYTEA                   NULL, -- 토큰
	"authentication" BYTEA                   NULL  -- 인증
)
WITH (
OIDS=false
);


-- 코드정보
CREATE TABLE "oauth_code"
(
	"code"           CHARACTER VARYING(256) NULL, -- 코드
	"authentication" BYTEA                   NULL  -- 인증
)
WITH (
OIDS=false
);


-- 승인정보
CREATE TABLE "oauth_approvals"
(
	"user_id"        CHARACTER VARYING(256)      NULL, -- 사용자ID
	"client_id"      CHARACTER VARYING(256)      NULL, -- 클라이언트ID
	"scope"          CHARACTER VARYING(256)      NULL, -- 접근범위
	"status"         CHARACTER VARYING(10)       NULL, -- 상태
	"expiresAt"      TIMESTAMP WITHOUT TIME ZONE NULL, -- 만료일시
	"lastModifiedAt" TIMESTAMP WITHOUT TIME ZONE NULL  -- 수정일시
)
WITH (
OIDS=false
);
