import { Role } from "./role";

export class User {
    id?: string;
    username?: string;
    password?: string;
    firstName?: string;
    lastName?: string;
    token?: string;
    isDeleting?: boolean;
    authorities?: Role;
}