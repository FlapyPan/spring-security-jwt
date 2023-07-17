# spring-security-jwt

Spring Security 使用 JWT token 登录认证的示例

特点：

- 无 Cookie 和 Session
- 后端无状态 (Stateless)
- 用户信息放置在 jwt token 中
- 无法注销下线

相关技术栈：

- Spring Security
- Json Web Token
- Spring Validation
- Mybatis

分支说明:

- `main` 使用 `jjwt` 库手动实现 filter 进行验证
- `oauth2` 使用 `oauth2-resource-server` 内置的 jwt 验证
