import { useEffect } from "react";
import { Stack, Typography, Button, Box } from "@mui/material";
import LogoutIcon from "@mui/icons-material/Logout";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../../account/providers/hooks";
import { firebaseAuth } from "../../../account/services/firebase-service";

export interface HeaderProps {
  title: string;
}

export function Header({ title }: HeaderProps) {
  const { firebaseUser } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await firebaseAuth.signOut();
    navigate("/");
  };

  useEffect(() => {
    if (firebaseUser !== undefined) {
      firebaseUser.getIdToken().then((token) => {
        console.log(token);
      });
    }
  }, [firebaseUser]);

  return (
    <Stack
      width="100%"
      sx={{ backgroundColor: "background.paper" }}
      alignItems="center"
      mb={4}
    >
      <Stack
        direction="row"
        justifyContent="space-between"
        alignItems="center"
        sx={{
          p: { xs: 2, md: 4 },
          width: "100%",
          maxWidth: "1400px",
        }}
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
          {title}
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
    </Stack>
  );
}
