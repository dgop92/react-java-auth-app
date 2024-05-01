import { Stack, Typography, Box, Divider, Button } from "@mui/material";
import OAuthSection from "./SignUp/OAuthSection";
import EmailSection from "./SignUp/EmailSection";

export function SignUp(): JSX.Element {
  return (
    <Box
      sx={{
        display: "flex",
        width: "100vw",
        height: "100vh",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Stack
        component="section"
        alignItems="center"
        sx={{
          justifyContent: "center",
          py: 4,
          m: 2,
          width: "100%",
          maxWidth: 600,
          backgroundColor: "#fff",
        }}
      >
        <Typography
          variant="h2"
          component="h2"
          sx={{
            fontFamily: "titleFontFamily",
            fontWeight: 700,
            textAlign: "center",
            fontSize: 48,
            padding: 1,
            mb: 14,
          }}
        >
          Sign Up
        </Typography>
        <Box sx={{ width: "85%" }}>
          <OAuthSection />
          <Stack direction="row" sx={{ width: "100%", my: 2 }}>
            <Stack sx={{ flexGrow: 1, justifyContent: "center", px: 1 }}>
              <Divider
                sx={{
                  borderColor: "text.secondary",
                  width: "100%",
                }}
              />
            </Stack>
            <Typography variant="body1" sx={{ color: "text.secondary" }}>
              Or sign up with email and password
            </Typography>
            <Stack sx={{ flexGrow: 1, justifyContent: "center", px: 1 }}>
              <Divider
                sx={{
                  borderColor: "text.secondary",
                  width: "100%",
                }}
              />
            </Stack>
          </Stack>
          <EmailSection />
        </Box>
      </Stack>
    </Box>
  );
}
