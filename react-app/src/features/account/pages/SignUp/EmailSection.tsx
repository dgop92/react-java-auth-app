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

export function EmailSection() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<SignUpData>({
    resolver: joiResolver(SignUpSchema),
  });

  const mutation = useMutation({
    mutationFn: async (data: SignUpData) => {
      await accountRepository.registerWithEmail(data);
    },
    onSuccess: async (_, variables) => {
      console.log("User registered successfully");
      try {
        await firebaseAuth.signInWithEmailAndPassword(
          variables.email,
          variables.password
        );
      } catch (error) {
        console.log("An error occurred while signing in the user");
      }
    },
    onError: (error) => {
      console.log("An error occurred while registering the user");
      console.log(error);
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
