import { Button, Divider, Stack, Typography } from "@mui/material";
import GoogleIcon from "@mui/icons-material/Google";
import { googleProvider } from "../../providers/google-provider";
import { firebaseAuth } from "../../services/firebase-service";
import { signInWithPopup } from "firebase/auth";

export function OAuthSection() {
  const handleGoogleSignIn = async () => {
    await signInWithPopup(firebaseAuth, googleProvider);
  };

  return (
    <>
      <Stack
        direction="row"
        alignItems="center"
        width="100%"
        sx={{ mt: { xs: 4, md: 8 }, mb: { xs: 4, md: 8 } }}
      >
        <Divider sx={{ flexGrow: 1 }} />
        <Typography variant="body1" fontWeight="500" px={2}>
          or
        </Typography>
        <Divider sx={{ flexGrow: 1 }} />
      </Stack>
      <Button
        color="primary"
        variant="outlined"
        startIcon={<GoogleIcon />}
        fullWidth
        sx={{ py: 1 }}
        onClick={handleGoogleSignIn}
      >
        Sing Up with Google
      </Button>
    </>
  );
}
