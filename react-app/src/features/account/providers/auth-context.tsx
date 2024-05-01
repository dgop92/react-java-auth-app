import { createContext } from "react";
import { FirebaseUser } from "../services/firebase-service";

export interface AuthContextProps {
  firebaseUser: FirebaseUser | undefined;
}

export const AuthContext = createContext<AuthContextProps>(
  {} as AuthContextProps
);
