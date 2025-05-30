import React from 'react';
import { useForm, Controller } from 'react-hook-form'; // For form handling and validation
import ReactDatePicker from 'react-datepicker'; // For data selection UI
import 'react-datepicker/dist/react-datepicker.css'; // Default styles for ReactDatePicker
import './SearchForm.css'; // Custom styles for this search form

/**
 * Helper function to allow only digits and essential control keys.
 * Prevents non-numeric characters, decimals, 'e', '+', '-' etc., from being typed.
 * Added when testing the application and realising that user could input 'e' as a number.
 */
const allowOnlyIntegers = (event) => {
  // Allow: backspace, delete, tab, escape, enter
  if ([46, 8, 9, 27, 13].indexOf(event.keyCode) !== -1 ||
    // Allow: Ctrl+A, Command+A
    (event.keyCode === 65 && (event.ctrlKey === true || event.metaKey === true)) ||
    // Allow: home, end, left, right, down, up
    (event.keyCode >= 35 && event.keyCode <= 40)) {
    // Let it happen, don't do anything
    return;
  }
  // Ensure that it is a number and stop the keypress if not
  if ((event.shiftKey || (event.keyCode < 48 || event.keyCode > 57)) && (event.keyCode < 96 || event.keyCode > 105)) {
    event.preventDefault();
  }
};

export default function SearchForm({ onSearch, isLoading }) {
  // Initialize React Hook Form (default values and validation mode)
  const {
    register, //Function to register input fields
    handleSubmit, // Function that handles form submission and automatically triggers validation
    control, // Connects controlled components (in my case ReactDatePicker) to React Hook Form
    getValues, // Function to retrieve form values
    formState: { errors } // Object containing form validation errors
  } = useForm({
    defaultValues: { // Initial values for form fields
      origin: '',
      destination: '',
      departureDate: null,
      returnDate: null,
      adults: 1,
      children: 0,
      infants: 0,
      currency: 'EUR'
    },
    mode: 'onTouched', // Validation mode: errors will show when a field loses focus after being interacted with
  });

  /**
   * Formats a JS Date object into a "yyyy-MM-dd" stribg.
   * Returns empty string if the input date is not valid.
   */
  const formatLocalDate = d => {
    if (!d) return '';
    const year = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${year}-${mm}-${dd}`;
  };
  
  /**
   * OnSubmit handles form submission.
   * It firstly processes the row format data before calling the onSearch function
   */
  const onSubmit = data => {
    // Last safe mesure before sending data, ensures that the numbers are finite and it has default values if they are not
    const adultsCount = Number.isFinite(data.adults) ? data.adults : 1;
    const childrenCount = Number.isFinite(data.children) ? data.children : 0;
    const infantsCount = Number.isFinite(data.infants) ? data.infants : 0;

    // Prepare the search parameters object before sending it to the backend
    const processedParams = {
      ...data,
      departureDate: data.departureDate ? formatLocalDate(data.departureDate) : '',
      returnDate: data.returnDate ? formatLocalDate(data.returnDate) : '',
      adults: adultsCount,
      children: childrenCount,
      infants: infantsCount,
    };
    console.log("Processed params being sent to backend:", processedParams); 
    onSearch(processedParams);
  };

  return (
    <form className="search-form" onSubmit={handleSubmit(onSubmit)} noValidate>
      <div className="field">
        <input
          {...register('origin', {
            required: 'Origin is required',
            pattern: { value: /^[A-Z]{3}$/, message: 'Must be 3 uppercase letters' }
          })}
          placeholder="Origin (e.g. SPU)" disabled={isLoading} />
        {errors.origin && <span className="error-message">{errors.origin.message}</span>}
      </div>
      <div className="field">
        <input
          {...register('destination', {
            required: 'Destination is required',
            pattern: { value: /^[A-Z]{3}$/, message: 'Must be 3 uppercase letters' }
          })}
          placeholder="Destination (e.g. NYC)" disabled={isLoading} />
        {errors.destination && <span className="error-message">{errors.destination.message}</span>}
      </div>
      <div className="field">
        <Controller
          name="departureDate" control={control} rules={{ required: 'Departure date is required' }}
          render={({ field }) => (
            <ReactDatePicker placeholderText="dd/mm/yyyy" dateFormat="dd/MM/yyyy" onChange={field.onChange} selected={field.value} className="date-input" minDate={new Date()} disabled={isLoading} />
          )} />
        {errors.departureDate && (<span className="error-message">{errors.departureDate.message}</span>)}
      </div>
      <div className="field">
        <Controller
          name="returnDate" control={control}
          rules={{
            validate: value => {
              const dep = getValues('departureDate');
              if (!value) return true; if (!dep) return true; 
              return value >= dep || 'Return date must be on/after departure';
            }
          }}
          render={({ field }) => (
            <ReactDatePicker placeholderText="dd/mm/yyyy" dateFormat="dd/MM/yyyy" onChange={field.onChange} selected={field.value} className="date-input" minDate={getValues('departureDate') || new Date()} disabled={isLoading} />
          )} />
        {errors.returnDate && (<span className="error-message">{errors.returnDate.message}</span>)}
      </div>

      <div className="field">
        <input
          type="number"
          min="1" // HTML5 native validation: minimum value is 1
          placeholder="Adults"
          disabled={isLoading}
          onKeyDown={allowOnlyIntegers} // Prevent non-integer characters
          {...register('adults', {
            required: 'Number of adults is required.',
            valueAsNumber: true,
            min: { value: 1, message: 'At least 1 adult is required.' },
            validate: (value) => !isNaN(value) || 'Adults count must be a valid number.'
          })}
        />
        {errors.adults && (<span className="error-message">{errors.adults.message}</span>)}
        <small className="field-helper-text">Ages 12+</small>
      </div>

      <div className="field">
        <input
          type="number"
          min="0" // HTML5 native validation
          placeholder="Children"
          disabled={isLoading}
          onKeyDown={allowOnlyIntegers} // Prevent non-integer characters
          {...register('children', {
            valueAsNumber: true,
            validate: {
              isNumber: value => !isNaN(value) || 'Children count is required (e.g., 0).',
              notNegative: value => isNaN(value) || value >= 0 || 'Children count cannot be negative.'
            }
          })}
        />
        {errors.children && (<span className="error-message">{errors.children.message}</span>)}
        <small className="field-helper-text">Ages 2-11</small>
      </div>

      <div className="field">
        <input
          type="number"
          min="0" // HTML5 native validation
          placeholder="Infants"
          disabled={isLoading}
          onKeyDown={allowOnlyIntegers} // Prevent non-integer characters
          {...register('infants', {
            valueAsNumber: true,
            validate: {
              isNumber: value => !isNaN(value) || 'Infants count is required (e.g., 0).',
              notNegative: value => isNaN(value) || value >= 0 || 'Infants count cannot be negative.'
            }
          })}
        />
        {errors.infants && (<span className="error-message">{errors.infants.message}</span>)}
        <small className="field-helper-text">Under 2 (on lap)</small>
      </div>

      <div className="field">
        <select {...register('currency', { required: true })} disabled={isLoading}>
          <option value="EUR">EUR</option>
          <option value="USD">USD</option>
          <option value="HRK">HRK</option>
        </select>
      </div>

      <div className="field field-button-wrapper">
        <button 
          type="submit" 
          className="search-button" 
          disabled={isLoading} // Disable button during loading to prevent user from interupting the search process with pressing the search button constantly
        >
          {isLoading ? 'Searching...' : 'Search Flights'}
        </button>
      </div>
    </form>
  );
}