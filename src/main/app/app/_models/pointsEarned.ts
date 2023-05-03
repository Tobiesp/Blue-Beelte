import { Student } from "./student";
import { PointCategory } from "./pointCategory";

export class PointsEarned {
    id?: string;
    eventDate?: string;
    student?: Student;
    pointCategory?: PointCategory;
    total?: number;
}