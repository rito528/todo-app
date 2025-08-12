import { z } from 'zod';

const todoStateSchema = z.enum(["Todo", "Progressing", "Done"])

const categorySchema = z.object({
  id: z.uint32(),
  name: z.string(),
  slug: z.string(),
  color: z.string(),
})

const todoSchema = z.object({
  id: z.uint32().nullable(),
  category: categorySchema.nullable(),
  title: z.string().max(255),
  body: z.string(),
  state: todoStateSchema
})

export type TodoState = z.infer<typeof todoStateSchema>

export type Todo = z.infer<typeof todoSchema>
