import { IconButton, InputAdornment, Stack, Typography } from "@mui/material";
import VisibilityIcon from "@mui/icons-material/VisibilityOutlined";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOffOutlined";
import { styled } from "@mui/material/styles";
import BaseTextField, {
  TextFieldProps as BaseTextFieldProps,
} from "@mui/material/TextField";
import { useState } from "react";

function TextFieldLabel({ label }: { label: string }): JSX.Element {
  return (
    <Typography variant="body1" color="initial" fontWeight="500">
      {label}
    </Typography>
  );
}

export type CustomTextFieldProps = BaseTextFieldProps & {
  name: string;
};

export const CustomTextField = styled((props: CustomTextFieldProps) => (
  <BaseTextField
    fullWidth
    size="small"
    margin="dense"
    variant="outlined"
    id={`${props.name}-id`}
    {...props}
  />
))<CustomTextFieldProps>(({ theme }) => ({
  "& .MuiInputBase-root": {
    borderRadius: theme.spacing(2),
  },
}));

export type TextFieldProps = Omit<BaseTextFieldProps, "label"> & {
  name: string;
  label: string;
};

export function TextField({ label, ...props }: TextFieldProps): JSX.Element {
  return (
    <Stack width="100%">
      <TextFieldLabel label={label} />
      <CustomTextField {...props} />
    </Stack>
  );
}

export type PasswordTextFieldProps = Omit<BaseTextFieldProps, "label"> & {
  extraInputProps: Record<string, unknown>;
  label: string;
};

export function PasswordTextField({
  extraInputProps,
  placeholder,
  label,
  ...props
}: PasswordTextFieldProps): JSX.Element {
  const [showPassword, setShowPassword] = useState(false);
  const handleClickShowPassword = () => setShowPassword(!showPassword);

  return (
    <Stack width="100%">
      <TextFieldLabel label={label} />
      <CustomTextField
        name="password"
        placeholder={placeholder || "Enter your password"}
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
    </Stack>
  );
}
