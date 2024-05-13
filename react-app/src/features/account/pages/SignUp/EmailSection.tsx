import {
  Box,
  Checkbox,
  LinearProgress,
  Link,
  Stack,
  Typography,
} from "@mui/material";
import { PasswordTextField, TextField } from "../../../../components/TextField";
import { PrimaryButton } from "../../../../components/buttons";
import { SubmitHandler, useForm } from "react-hook-form";
import { joiResolver } from "@hookform/resolvers/joi";
import { SignUpData, SignUpSchema } from "../../schemas/signup-schema";
import { useMutation } from "@tanstack/react-query";
import { accountRepository } from "../../repositories/factory";
import { firebaseAuth } from "../../services/firebase-service";
import { useSnackbar } from "notistack";
import { APIError } from "../../../../common/errors";
import { ERROR_SNACKBAR_OPTIONS } from "../../../../utils/customSnackbar";

export function EmailSection() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<SignUpData>({
    resolver: joiResolver(SignUpSchema),
  });

  const { enqueueSnackbar } = useSnackbar();

  const mutation = useMutation({
    mutationFn: async (data: SignUpData) => {
      await accountRepository.registerWithEmail(data);
    },
    onSuccess: async (_, variables) => {
      try {
        await firebaseAuth.signInWithEmailAndPassword(
          variables.email,
          variables.password
        );
      } catch (error) {
        enqueueSnackbar(
          "Sorry, something went wrong, go to login page and try again",
          ERROR_SNACKBAR_OPTIONS
        );
      }
    },
    onError: (error) => {
      if (error instanceof APIError) {
        if (error.statusCode === 409) {
          enqueueSnackbar(
            "Sorry, this email is already in use, try another",
            ERROR_SNACKBAR_OPTIONS
          );

          return;
        }
      }
      enqueueSnackbar(
        "Sorry, something went wrong, try again later",
        ERROR_SNACKBAR_OPTIONS
      );
    },
  });

  const onSubmit: SubmitHandler<SignUpData> = (data) => {
    mutation.mutate(data);
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
      <Stack direction="row" mb={4} alignItems="center" width="100%">
        <Checkbox />
        <Typography variant="body1" fontWeight="500">
          I agree to the <Link href="#">Terms of Service and Policy</Link>
        </Typography>
      </Stack>

      {mutation.isPending ? (
        <Box width="100%">
          <LinearProgress />
        </Box>
      ) : (
        <PrimaryButton size="small" type="submit" sx={{ width: "100%" }}>
          Signup
        </PrimaryButton>
      )}
    </Stack>
  );
}
