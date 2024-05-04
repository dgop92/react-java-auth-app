import { googleProvider } from "../../providers/google-provider";
import { firebaseAuth } from "../../services/firebase-service";
import { signInWithPopup } from "firebase/auth";
import { OAuthSection } from "../common/OAuthSection";

export function LoginOAuthSection() {
  const handleGoogleSignIn = async () => {
    await signInWithPopup(firebaseAuth, googleProvider);
  };

  return (
    <OAuthSection
      onGoogleClick={handleGoogleSignIn}
      googleLabel="Sign in with Google"
    />
  );
}
