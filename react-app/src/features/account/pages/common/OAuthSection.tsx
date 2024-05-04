import { Button, Divider, Stack, Typography } from "@mui/material";
import GoogleIcon from "@mui/icons-material/Google";

export interface OAuthSectionProps {
  onGoogleClick: () => void;
  googleLabel: string;
}

export function OAuthSection({
  onGoogleClick,
  googleLabel,
}: OAuthSectionProps) {
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
        onClick={onGoogleClick}
      >
        {googleLabel}
      </Button>
    </>
  );
}
