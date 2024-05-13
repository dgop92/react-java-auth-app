import { FullPageBox } from "../../components/Layout/FullPageBox";
import { Typography, Container, Stack } from "@mui/material";
import { PrimaryButton } from "../../components/buttons";
import { useNavigate } from "react-router-dom";

export function HomePage() {
  const navigate = useNavigate();

  return (
    <FullPageBox justifyContent="center" alignItems="center">
      <Container
        maxWidth="xl"
        sx={{ display: "flex", flexDirection: "column", alignItems: "center" }}
      >
        <Typography
          variant="h1"
          component="h1"
          textAlign="center"
          sx={{
            fontFamily: "titleFontFamily",
            fontWeight: "medium",
            fontSize: { xs: 36, lg: 48 },
            mb: 1,
          }}
        >
          React + Spring Boot API
        </Typography>
        <Typography
          variant="h3"
          component="h3"
          textAlign="center"
          sx={{
            fontFamily: "titleFontFamily",
            fontWeight: "medium",
            fontSize: { xs: 18, lg: 24 },
          }}
        >
          an example of a React front-end application that consumes a Rest API
          built with Java Spring Boot.
        </Typography>

        <Stack
          gap={1}
          mt={4}
          sx={{
            flexDirection: { xs: "column", sm: "row" },
            width: "100%",
            maxWidth: "500px",
          }}
        >
          <PrimaryButton
            size="small"
            sx={{ width: "100%" }}
            onClick={() => navigate("/login")}
          >
            Go to Log in
          </PrimaryButton>
          <PrimaryButton
            size="small"
            sx={{ width: "100%" }}
            onClick={() => navigate("/signup")}
          >
            Go to Sign up
          </PrimaryButton>
        </Stack>
      </Container>
    </FullPageBox>
  );
}
