import { Link, Stack, Typography } from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import { EmailSection } from "./EmailSection";
import { OAuthSection } from "./OAuthSection";

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
            mb: { xs: 7, md: 14 },
          }}
        >
          Get Started Now
        </Typography>
        <EmailSection />
        <OAuthSection />
        <Typography variant="body1" fontWeight={500} textAlign="center" my={2}>
          Have an account?{" "}
          <Link component={RouterLink} to="/login" underline="none">
            Log in
          </Link>
        </Typography>
      </Stack>
    </Stack>
  );
}
