import { ThemeProvider } from "@mui/material/styles";
import { BrowserRouter } from "react-router-dom";
import { muiTheme } from "../styles/theme";
import { AuthProvider } from "../features/account/providers/AuthProvider";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

interface AppProviderProps {
  children: React.ReactNode;
}

// Create a client
const queryClient = new QueryClient();

export function AppProvider({ children }: AppProviderProps) {
  return (
    <BrowserRouter>
      <AuthProvider>
        <ThemeProvider theme={muiTheme}>
          <QueryClientProvider client={queryClient}>
            {children}
          </QueryClientProvider>
        </ThemeProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}
