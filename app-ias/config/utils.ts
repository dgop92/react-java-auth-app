import { resolve } from "path";

/**
 * Returns the stack name based on the provided application name and environment.
 * @param appName - The name of the application.
 * @param stackType - The type of stack (e.g., compute, storage, etc.).
 * @param env - The environment (e.g., development, production).
 * @returns The stack name in the format "appName-env-stack".
 */
export function getStackName(appName: string, stackType: string, env: string) {
  return `${appName}-${stackType}-${env}-stack`;
}

/**
 * Generates a unique CloudFormation ID by combining the provided ID and name.
 * @param id - The ID is the stack name.
 * @param name - Your custom name for the resource.
 * @returns The generated CloudFormation ID.
 */
export function getCloudFormationID(id: string, name: string) {
  return `${id}-${name}-id`;
}

/**
 * Generates a resource name by concatenating an ID and a name.
 * @param id - The ID is the stack name.
 * @param name - Your custom name for the resource.
 * @returns The generated resource name.
 */
export function getResourceName(id: string, name: string) {
  return `${id}-${name}`;
}

/**
 * Retrieves the root directory of an external project inside the mono-repo.
 *
 * TODO: Find a better way of finding the cwd of the file that calls this function.
 *
 * @param projectName - The name of the project.
 * @returns The root directory of the external project.
 * @throws Error if no project name is provided.
 */
export function getRootOfExternalProject(projectName: string) {
  if (!projectName || projectName.length === 0) {
    throw new Error("You must provide a project name.");
  }
  const root = resolve(process.cwd(), `../${projectName}`);
  return root;
}
