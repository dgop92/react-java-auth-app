import { createTheme, alpha } from "@mui/material";
import { SxProps } from "@mui/system";

declare module "@mui/material/styles/createTypography" {
  interface FontStyle {
    titleFontFamily: string;
  }
}

// TODO: dark and light colors are assumptions, change with the real one
export const muiTheme = createTheme({
  palette: {
    primary: {
      main: "#5B7B7A",
      light: "#729796",
      dark: "#455E5D",
      contrastText: "#fff",
    },
    secondary: {
      main: "#EB6F47",
      light: "#F09375",
      dark: "#E64B19",
      contrastText: "#fff",
    },
    background: {
      default: "hsla(200, 27.3%, 97.8%, 1)",
      paper: "#fff",
    },
    text: {
      primary: "#323232",
      secondary: "#969696",
    },
  },
  typography: {
    fontFamily: '"Poppins", sans-serif',
    titleFontFamily: '"Poppins", sans-serif',
  },

  // Disable some google default styles

  components: {
    MuiButton: {
      defaultProps: {
        disableFocusRipple: true,
        disableElevation: true,
        disableRipple: true,
      },
      styleOverrides: {
        root: {
          textTransform: "capitalize",
        },
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        containedPrimary: ({ theme }) => ({
          "&:hover": {
            backgroundColor: alpha(theme.palette.primary.main, 0.85),
          },
        }),
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        containedSecondary: ({ theme }) => ({
          "&:hover": {
            backgroundColor: alpha(theme.palette.secondary.main, 0.85),
          },
        }),
      },
    },
    MuiIconButton: {
      styleOverrides: {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        colorPrimary: ({ theme }) => ({
          "&:hover": {
            backgroundColor: alpha(theme.palette.primary.main, 0.85),
          },
        }),
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        colorSecondary: ({ theme }) => ({
          "&:hover": {
            backgroundColor: alpha(theme.palette.secondary.main, 0.85),
          },
        }),
      },
    },
  },
});

export type MuiTheme = typeof muiTheme;
export type SxThemeProps = SxProps<MuiTheme>;
