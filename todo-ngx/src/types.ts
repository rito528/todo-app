import { z } from 'zod';

export const todoStateSchema = z.enum(["Todo", "Progressing", "Done"])

export const categorySchema = z.object({
  id: z.uint32(),
  name: z.string(),
  slug: z.string(),
  color: z.string(),
})

export const todoSchema = z.object({
  id: z.uint32().nullable(),
  category: categorySchema.nullable(),
  title: z.string().max(255),
  body: z.string(),
  state: todoStateSchema
})

export type TodoState = z.infer<typeof todoStateSchema>

export type Category = z.infer<typeof categorySchema>

export type Todo = z.infer<typeof todoSchema>
