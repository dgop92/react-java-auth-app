import { Link, Stack, Typography } from "@mui/material";
import { PasswordTextField, TextField } from "../../../../components/TextField";
import { SecondaryButton } from "../../../../components/buttons";
import { SubmitHandler, useForm } from "react-hook-form";
import { joiResolver } from "@hookform/resolvers/joi";
import { SignUpData, SignUpSchema } from "../../schemas/signup-schema";
import { firebaseAuth } from "../../services/firebase-service";

export default function EmailSection() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<SignUpData>({
    resolver: joiResolver(SignUpSchema),
  });

  const onSubmit: SubmitHandler<SignUpData> = async (data) => {
    const r = await firebaseAuth.createUserWithEmailAndPassword(
      data.email,
      data.password
    );
    console.log(r);
  };

  return (
    <Stack
      component="form"
      alignItems="center"
      sx={{ width: "100%" }}
      onSubmit={handleSubmit(onSubmit)}
    >
      <TextField
        name="email"
        label="Email"
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
      <SecondaryButton type="submit" sx={{ py: 1.5, width: "100%", mt: 4 }}>
        Continue
      </SecondaryButton>
      <Typography
        variant="body1"
        sx={{
          fontSize: "0.875rem",
          p: 1,
        }}
      >
        Already an account? <Link href="/login">Log in</Link>
      </Typography>
    </Stack>
  );
}
