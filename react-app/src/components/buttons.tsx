import Button, { ButtonProps } from '@mui/material/Button';
import { styled } from '@mui/material/styles';

export const PrimaryButton = styled((props: ButtonProps) => (
  <Button color="primary" variant="contained" {...props}>
    {props?.children}
  </Button>
))(({ theme }) => ({
  fontWeight: 500,
  borderRadius: theme.spacing(1.75),
}));

export const SecondaryButton = styled((props: ButtonProps) => (
  <Button color="secondary" variant="contained" {...props}>
    {props?.children}
  </Button>
))(({ theme }) => ({
  fontWeight: 500,
  borderRadius: theme.spacing(1.75),
}));
