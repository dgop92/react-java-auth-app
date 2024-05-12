import { Box, Divider, LinearProgress, Stack, Typography } from "@mui/material";
import { TextField } from "../../../../components/TextField";
import { PrimaryButton } from "../../../../components/buttons";
import {
  UpdateProfileData,
  UpdateProfileSchema,
} from "../../schemas/update-profile-schema";
import { SubmitHandler, useForm } from "react-hook-form";
import { joiResolver } from "@hookform/resolvers/joi";
import { accountRepository } from "../../../account/repositories/factory";
import { useMutation } from "@tanstack/react-query";

export function BasicInfo() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<UpdateProfileData>({
    resolver: joiResolver(UpdateProfileSchema),
  });

  const mutation = useMutation({
    mutationFn: async (data: UpdateProfileData) => {
      await accountRepository.updateProfile(data);
    },
    onSuccess: async () => {
      console.log("Profile updated successfully");
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const onSubmit: SubmitHandler<UpdateProfileData> = (data) => {
    console.log(data);
    mutation.mutate(data);
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        maxWidth: "700px",
        backgroundColor: "background.paper",
        borderRadius: "16px",
        mx: { xs: 1, sm: 2, md: 4 },
      }}
    >
      <Typography variant="body1" fontSize={18} fontWeight="bold" p={2}>
        Basic information
      </Typography>
      <Divider />
      <Stack
        component="form"
        alignItems="center"
        width="100%"
        sx={{ p: { xs: 2, md: 4 } }}
        onSubmit={handleSubmit(onSubmit)}
      >
        <Stack gap={1.5} width="100%" mb={3}>
          <TextField
            label="First Name"
            name="firstName"
            placeholder="Enter your first name"
            inputProps={{
              ...register("firstName"),
            }}
            error={!!errors.firstName}
            helperText={errors.firstName?.message}
          />
          <TextField
            label="Last Name"
            name="lastName"
            placeholder="Enter your last name"
            inputProps={{
              ...register("lastName"),
            }}
            error={!!errors.lastName}
            helperText={errors.lastName?.message}
          />
        </Stack>

        {mutation.isPending ? (
          <Box width="100%">
            <LinearProgress />
          </Box>
        ) : (
          <PrimaryButton
            size="small"
            type="submit"
            sx={{
              width: "fit-content",
              px: 6,
              alignSelf: "flex-end",
            }}
          >
            Update
          </PrimaryButton>
        )}
      </Stack>
    </Box>
  );
}
