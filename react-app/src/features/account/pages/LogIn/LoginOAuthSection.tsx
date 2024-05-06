import { googleProvider } from "../../providers/google-provider";
import { firebaseAuth } from "../../services/firebase-service";
import { signInWithPopup } from "firebase/auth";
import { OAuthSection } from "../common/OAuthSection";

export function LoginOAuthSection() {
  const handleGoogleSignIn = async () => {
    try {
      await signInWithPopup(firebaseAuth, googleProvider);
    } catch (error) {
      console.error("Error signing in with Google: ", error);
    }
  };

  return (
    <OAuthSection
      onGoogleClick={handleGoogleSignIn}
      googleLabel="Sign in with Google"
    />
  );
}
