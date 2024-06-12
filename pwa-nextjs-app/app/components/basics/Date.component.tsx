import { ChangeEvent, useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css'; // Need to import this lib so that the calendar is showed  properly


export default function DateField(props) {
    const [selectedDate, setSelectedDate] = useState<Date | null>(props.value);

    const handelOnChange = (e: ChangeEvent<HTMLInputElement>, date: Date | null) => {
        setSelectedDate(date);
        if( date != null ) {
            e.target.value = date.toISOString();
        }
        else {
            e.target.value = "";
        }
        
        if( props.onChange ) {
            props.onChange(e);
        }
    }

    return (
        <DatePicker
            disabled={props.disabled}
            style={{width: "100%"}}
            className={`${props.className} custom-datepicker`}
            selected={selectedDate}
            onChange={(date: Date | null, e: ChangeEvent<HTMLInputElement>) => handelOnChange(e, date)}
            dateFormat="yyyy-MM-dd"
        />
    );
};
