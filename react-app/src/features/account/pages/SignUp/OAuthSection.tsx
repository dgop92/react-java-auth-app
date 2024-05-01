import { Button } from "@mui/material";
import GoogleIcon from "@mui/icons-material/Google";
import { googleProvider } from "../../providers/google-provider";
import { firebaseAuth } from "../../services/firebase-service";
import { signInWithPopup } from "firebase/auth";

export default function OAuthSection() {
  const handleGoogleSignIn = async () => {
    await signInWithPopup(firebaseAuth, googleProvider);
  };

  return (
    <Button
      color="secondary"
      variant="outlined"
      startIcon={<GoogleIcon />}
      fullWidth
      sx={{ py: 1, mb: 2 }}
      onClick={handleGoogleSignIn}
    >
      Sing Up with Google
    </Button>
  );
}
