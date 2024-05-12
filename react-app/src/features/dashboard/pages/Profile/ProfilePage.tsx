import { Container } from "@mui/material";
import { FullPageBox } from "../../../../components/Layout/FullPageBox";
import { Header } from "../common/Header";
import { BasicInfo } from "./BasicInfo";

export function ProfilePage() {
  return (
    <FullPageBox>
      <Header title="Profile" />
      <Container maxWidth="xl">
        <BasicInfo />
      </Container>
    </FullPageBox>
  );
}
