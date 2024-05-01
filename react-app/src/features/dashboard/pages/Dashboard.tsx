import { useEffect } from "react";
import { useAuth } from "../../account/providers/hooks";
import { Box, Stack, Typography, Button } from "@mui/material";
import { firebaseAuth } from "../../account/services/firebase-service";
import LogoutIcon from "@mui/icons-material/Logout";

export default function Dashboard() {
  const { firebaseUser } = useAuth();

  const handleLogout = async () => {
    await firebaseAuth.signOut();
  };

  useEffect(() => {
    if (firebaseUser !== undefined) {
      firebaseUser.getIdToken().then((token) => {
        console.log(token);
      });
    }
  }, [firebaseUser]);

  return (
    <Box
      sx={{
        display: "flex",
        width: "100vw",
        overflowY: "hidden",
      }}
    >
      <Stack
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        sx={{ px: 4, py: 2, width: "100%" }}
      >
        <Typography
          variant="h2"
          component="h2"
          sx={{
            fontFamily: "titleFontFamily",
            fontWeight: 700,
            textAlign: "center",
            fontSize: 36,
          }}
        >
          Dashboard
        </Typography>
        <Button
          aria-label="log-out"
          onClick={handleLogout}
          size="medium"
          variant="contained"
          endIcon={<LogoutIcon />}
        >
          Logout
        </Button>
      </Stack>
    </Box>
  );
}
