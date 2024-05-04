import { Link, Stack, Typography } from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import { EmailSection } from "./EmailSection";
import { LoginOAuthSection } from "./LoginOAuthSection";

export function FormSide() {
  return (
    <Stack alignItems="center" justifyContent="center" width="100%">
      <Stack
        sx={{
          maxWidth: "500px",
          width: "100%",
          px: { xs: 3, md: 4 },
          py: { xs: 4, md: 0 },
        }}
      >
        <Typography
          variant="h2"
          component="h2"
          sx={{
            fontFamily: "titleFontFamily",
            fontWeight: "medium",
            fontSize: 32,
            mb: 1,
          }}
        >
          Welcome back!
        </Typography>
        <Typography
          variant="body1"
          sx={{
            fontWeight: "medium",
            fontSize: 16,
            mb: { xs: 7, md: 14 },
          }}
        >
          Enter your credentials to access your account
        </Typography>
        <EmailSection />
        <LoginOAuthSection />
        <Typography variant="body1" fontWeight={500} textAlign="center" my={2}>
          Don't have an account?{" "}
          <Link component={RouterLink} to="/signup" underline="none">
            Sign up
          </Link>
        </Typography>
      </Stack>
    </Stack>
  );
}
