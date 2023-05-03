import { Group } from "./group";
import { PointCategory } from "./pointCategory";

export class PointType {
    id?: string;
    pointCategory?: PointCategory;
    group?: Group;
    enabled?: boolean;
    totalPoints?: number;
}