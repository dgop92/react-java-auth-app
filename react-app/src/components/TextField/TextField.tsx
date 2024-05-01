import { IconButton, InputAdornment } from "@mui/material";
import VisibilityIcon from "@mui/icons-material/VisibilityOutlined";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOffOutlined";
import { styled } from "@mui/material/styles";
import BaseTextField, {
  TextFieldProps as BaseTextFieldProps,
} from "@mui/material/TextField";
import { useState } from "react";

export type TextFieldProps = BaseTextFieldProps & {
  name: string;
};

export const TextField = styled((props: TextFieldProps) => (
  <BaseTextField
    fullWidth
    size="small"
    margin="normal"
    id={`${props.name}-id`}
    {...props}
  />
))<TextFieldProps>(({ theme }) => ({
  "& .MuiInputBase-root": {
    borderRadius: theme.spacing(2),
  },
  "& .MuiInputBase-input": {
    padding: `${theme.spacing(1.5)} ${theme.spacing(3)}`,
    py: 1.5,
  },
  "& .MuiInputLabel-root": {
    top: "3px",
  },
}));

export type PasswordTextFieldProps = BaseTextFieldProps & {
  extraInputProps: Record<string, unknown>;
};

export function PasswordTextField({
  extraInputProps,
  label,
  ...props
}: PasswordTextFieldProps): JSX.Element {
  const [showPassword, setShowPassword] = useState(false);
  const handleClickShowPassword = () => setShowPassword(!showPassword);

  return (
    <TextField
      name="password"
      label={label || "Password"}
      InputProps={{
        type: showPassword ? "text" : "password",
        autoComplete: "new-password",
        endAdornment: (
          <InputAdornment position="end">
            <IconButton
              aria-label="toggle password visibility"
              onClick={handleClickShowPassword}
            >
              {showPassword ? (
                <VisibilityIcon color="secondary" />
              ) : (
                <VisibilityOffIcon color="secondary" />
              )}
            </IconButton>
          </InputAdornment>
        ),
        ...extraInputProps,
      }}
      {...props}
    />
  );
}
