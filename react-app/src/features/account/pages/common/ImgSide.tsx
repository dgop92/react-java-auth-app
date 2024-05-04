import { Box } from "@mui/material";

export function ImgSide() {
  return (
    <Box display="flex" height="100vh">
      <Box
        component="img"
        src="/plants.jpg"
        alt="green leaves of a plant"
        sx={{
          objectFit: "cover",
          width: "100%",
          height: "100%",
        }}
      />
    </Box>
  );
}
