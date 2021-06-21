class DateTimeUtil{

    public static toAPIDate(date : Date | undefined):string | null{
        if (date){
            const month = date.getMonth()+1;
            const d1 = month < 10 ? '-0' : '-';
            const day = date.getDate();
            const d2 = day < 10 ? '-0' : '-';
            return `${date.getFullYear()}${d1}${month}${d2}${day}`;
        }
        return null;
    }

    public static toAPITime(date : Date | undefined):string | null{
        if (date){
            const hours = date.getHours();
            const d1 = hours < 10 ? '0' : '';
            const min = date.getMinutes();
            const d2 = min < 10 ? ':0' : ':';
            const sec = date.getSeconds();
            const d3 = sec < 10 ? ':0' : ':';
            return `${d1}${hours}${d2}${min}${d3}${sec}`;
        }
        return null;
    }

    public static toAPIDateTime(date : Date | undefined): string | null{
        if (date){
            return `${this.toAPIDate(date)} ${this.toAPITime(date)}`;
        }
        return null;
    }

    public static fromAPIDateTime(strDate : string | undefined): Date | undefined{
        //2020-06-26 16:47:00
        //          111111111
        //0123456789012345678
        if (strDate){
            const year = +strDate.substr(0, 4);
            const month = +strDate.substr(5, 2);
            const day = +strDate.substr(8, 2);
            const hour = +strDate.substr(11, 2);
            const min = +strDate.substr(14, 2);
            const sec = +strDate.substr(17, 2);
            return new Date(year, month-1, day, hour, min, sec);
        }
        return undefined;
    }

    public static fromAPIDate(strDate : string | undefined): Date | undefined{
        if (strDate){
            const year = +strDate.substr(0, 4);
            const month = +strDate.substr(5, 2);
            const day = +strDate.substr(8, 2);
            return new Date(year, month-1, day);
        }
        return undefined;
    }

    public static fromTextField(strDate : string | null): Date | undefined{
        if (strDate !== null){
            const year = +strDate.substr(0, 4);
            const month = +strDate.substr(5, 2);
            const day = +strDate.substr(8, 2);
            return new Date(year, month-1, day);
        }
        return undefined;
    }

}

export default DateTimeUtil;