import Button, { ButtonProps } from "@mui/material/Button";
import { styled } from "@mui/material/styles";

export const PrimaryButton = styled((props: ButtonProps) => (
  <Button color="primary" variant="contained" {...props}>
    {props?.children}
  </Button>
))(({ theme }) => ({
  fontWeight: "bold",
  borderRadius: theme.spacing(1.5),
  padding: theme.spacing(1),
  fontSize: theme.typography.pxToRem(13),
}));

export const SecondaryButton = styled((props: ButtonProps) => (
  <Button color="secondary" variant="contained" {...props}>
    {props?.children}
  </Button>
))(({ theme }) => ({
  fontWeight: "bold",
  borderRadius: theme.spacing(1.75),
  p: 1.5,
}));
