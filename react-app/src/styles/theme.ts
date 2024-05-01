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
      main: "hsla(228, 20.3%, 25.1%, 1)",
      light: "hsla(228, 20.3%, 30%, 1)",
      dark: "hsla(228, 19.6%, 20%, 1)",
      contrastText: "#fff",
    },
    secondary: {
      main: "hsla(208, 37.1%, 48%, 1);",
      light: "hsla(208, 46.9%, 55.7%, 1)",
      dark: "hsla(207, 37%, 36.1%, 1)",
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
    fontFamily: '"Quicksand", "Helvetica", "Arial", sans-serif',
    titleFontFamily: '"Montserrat", "Helvetica", "Arial", sans-serif',
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
