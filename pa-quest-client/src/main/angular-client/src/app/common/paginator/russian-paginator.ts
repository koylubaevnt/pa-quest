import { MatPaginatorIntl } from '@angular/material';

export class RussianPaginator extends MatPaginatorIntl {

    itemsPerPageLabel: string = "Элементов на странице";
    firstPageLabel: string = "Первая страница";
    lastPageLabel: string = "Последняя страница";
    nextPageLabel: string = "Следующая страница";
    previousPageLabel: string = "Предыдущая страница";
    ofLabel: string = "из";
    
    getRangeLabel = function (page, pageSize, length) {
        if (length === 0 || pageSize === 0) {
            return `0 ${this.ofLabel} ${length}`; }
        length = Math.max(length, 0); 
        const startIndex = page * pageSize; 
        const endIndex = startIndex < length ? 
                            Math.min(startIndex + pageSize, length) :
                            startIndex + pageSize; 
        return `${startIndex + 1} - ${endIndex} ${this.ofLabel} ${length}`; 
    }

  }