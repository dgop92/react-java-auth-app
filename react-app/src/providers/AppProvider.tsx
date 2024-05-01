import { ThemeProvider } from "@mui/material/styles";
import { BrowserRouter } from "react-router-dom";
import { muiTheme } from "../styles/theme";
import { AuthProvider } from "../features/account/providers/AuthProvider";

interface AppProviderProps {
  children: React.ReactNode;
}

export function AppProvider({ children }: AppProviderProps) {
  return (
    <AuthProvider>
      <ThemeProvider theme={muiTheme}>
        <BrowserRouter>{children}</BrowserRouter>
      </ThemeProvider>
    </AuthProvider>
  );
}
