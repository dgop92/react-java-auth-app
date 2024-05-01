import { CssBaseline } from "@mui/material";
import { AppProvider } from "./providers/AppProvider";
import MainRoutes from "./routes/MainRoutes";

function App() {
  return (
    <AppProvider>
      <CssBaseline />
      <MainRoutes />
    </AppProvider>
  );
}

export default App;
