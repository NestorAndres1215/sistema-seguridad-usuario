export interface TokenResponse {
  message: string;
  token: string;
  user: UserLogin;
  expiration: string;
}

export interface UserLogin {
  id: number;
  username: string;
  email: string;
  role: string;
}
