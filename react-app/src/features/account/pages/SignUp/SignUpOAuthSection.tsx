import { googleProvider } from "../../providers/google-provider";
import { firebaseAuth } from "../../services/firebase-service";
import { signInWithPopup } from "firebase/auth";
import { OAuthSection } from "../common/OAuthSection";
import { enqueueSnackbar } from "notistack";
import { ERROR_SNACKBAR_OPTIONS } from "../../../../utils/customSnackbar";

export function SignUpOAuthSection() {
  const handleGoogleSignIn = async () => {
    try {
      await signInWithPopup(firebaseAuth, googleProvider);
    } catch (error) {
      enqueueSnackbar(
        "Sorry, something went wrong, try again later",
        ERROR_SNACKBAR_OPTIONS
      );
    }
  };

  return (
    <OAuthSection
      onGoogleClick={handleGoogleSignIn}
      googleLabel="Sign up with Google"
    />
  );
}
