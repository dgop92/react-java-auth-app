import { Stack, Typography, Box } from "@mui/material";

export function Login(): JSX.Element {
  return (
    <Box
      sx={{
        display: "flex",
        width: "100vw",
        height: "100vh",
        overflowY: "hidden",
      }}
    >
      <Stack
        component="section"
        alignItems="center"
        sx={{
          justifyContent: "center",
          py: 0,
          width: { xs: "100%", md: "50%" },
          overflowY: "auto",
          zIndex: 2,
          postion: "relative",
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
            mb: 6,
          }}
        >
          Login
        </Typography>
      </Stack>
    </Box>
  );
}
