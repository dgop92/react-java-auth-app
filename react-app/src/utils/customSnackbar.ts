import { EnqueueSnackbar } from "notistack";

type SnackbarOptions = Parameters<EnqueueSnackbar>[1];

export const ERROR_SNACKBAR_OPTIONS: SnackbarOptions = {
  variant: "error",
  autoHideDuration: 6000,
  anchorOrigin: { vertical: "bottom", horizontal: "right" },
};

export const SUCCESS_SNACKBAR_OPTIONS: SnackbarOptions = {
  variant: "success",
  anchorOrigin: { vertical: "bottom", horizontal: "right" },
};
