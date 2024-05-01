import { useState, useEffect } from "react";
import { firebaseAuth, FirebaseUser } from "../services/firebase-service";
import { AuthContext } from "./auth-context";

interface AuthProviderProps {
  children: React.ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [currentUser, setCurrentUser] = useState<FirebaseUser | undefined>(
    undefined
  );
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const unsubscribe = firebaseAuth.onAuthStateChanged((user) => {
      if (user) {
        setCurrentUser(user);
      }

      setLoading(false);
      console.log(user);
    });

    return unsubscribe;
  }, []);

  // TODO: use a loading component
  return (
    <AuthContext.Provider value={{ firebaseUser: currentUser }}>
      {!loading && children}
    </AuthContext.Provider>
  );
}
