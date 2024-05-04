import Stack, { StackProps } from "@mui/material/Stack";
import { styled } from "@mui/material/styles";

export const FullPageBox = styled((props: StackProps) => (
  <Stack {...props}>{props?.children}</Stack>
))(() => ({
  width: "100vw",
  height: "100vh",
}));
