import { Link, Stack } from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import { PasswordTextField, TextField } from "../../../../components/TextField";
import { PrimaryButton } from "../../../../components/buttons";
import { SubmitHandler, useForm } from "react-hook-form";
import { joiResolver } from "@hookform/resolvers/joi";
import { LogInData, LogInSchema } from "../../schemas/login-schema";
import { firebaseAuth } from "../../services/firebase-service";
import { useSnackbar } from "notistack";
import { ERROR_SNACKBAR_OPTIONS } from "../../../../utils/customSnackbar";

export function EmailSection() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LogInData>({
    resolver: joiResolver(LogInSchema),
  });

  const { enqueueSnackbar } = useSnackbar();

  const onSubmit: SubmitHandler<LogInData> = async (data) => {
    try {
      await firebaseAuth.signInWithEmailAndPassword(data.email, data.password);
    } catch (error) {
      enqueueSnackbar(
        "Sorry, something went wrong, try again later",
        ERROR_SNACKBAR_OPTIONS
      );
    }
  };

  return (
    <Stack
      component="form"
      alignItems="center"
      width="100%"
      onSubmit={handleSubmit(onSubmit)}
    >
      <Stack gap={1.5} mb={4} width="100%">
        <TextField
          label="Email"
          name="email"
          autoComplete="email"
          placeholder="Enter your email address"
          inputProps={{
            ...register("email"),
          }}
          error={!!errors.email}
          helperText={errors.email?.message}
        />
        <PasswordTextField
          label="Password"
          extraInputProps={{ ...register("password") }}
          error={!!errors.password}
          helperText={errors.password?.message}
        />
      </Stack>
      <Link
        textAlign="left"
        variant="body1"
        fontWeight={500}
        mb={4}
        fontSize={12}
        component={RouterLink}
        to="/forgot-password"
        underline="none"
        width="100%"
        color="text.primary"
      >
        ¿Forgot your password?
      </Link>
      <PrimaryButton size="small" type="submit" sx={{ width: "100%" }}>
        Log in
      </PrimaryButton>
    </Stack>
  );
}
