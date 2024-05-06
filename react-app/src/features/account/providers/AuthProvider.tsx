import { useState, useEffect } from "react";
import { firebaseAuth, FirebaseUser } from "../services/firebase-service";
import { AuthContext } from "./auth-context";
import { useNavigate } from "react-router-dom";

interface AuthProviderProps {
  children: React.ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [currentUser, setCurrentUser] = useState<FirebaseUser | undefined>(
    undefined
  );
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const unsubscribe = firebaseAuth.onAuthStateChanged((user) => {
      if (user) {
        setCurrentUser(user);
        navigate("/dashboard");
      }

      setLoading(false);
      console.log("User: ", user);
    });

    return unsubscribe;
  }, [navigate]);

  // TODO: use a loading component
  return (
    <AuthContext.Provider value={{ firebaseUser: currentUser }}>
      {!loading && children}
    </AuthContext.Provider>
  );
}
