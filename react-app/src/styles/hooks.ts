import { useMediaQuery } from "@mui/material";
import { MuiTheme } from "./theme";

interface ThemeMediaQuery {
  (queryInput: (theme: MuiTheme) => string): boolean;
}

export const useThemeMediaQuery: ThemeMediaQuery = useMediaQuery;
