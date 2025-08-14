import { Pipe, PipeTransform } from '@angular/core';
import { TodoState } from '../../types';

@Pipe({
  name: 'todoState'
})
export class TodoStatePipe implements PipeTransform {

  transform(value: TodoState, ...args: string[]) {
    if (value === "Todo") {
      return "TODO(着手前)"
    } else if (value === "Progressing") {
      return "進行中"
    } else if (value === "Done") {
      return "完了"
    } else {
      const _exhaustiveCheck: never = value
      throw new Error("Unreachable: " + _exhaustiveCheck)
    }
  }

}
