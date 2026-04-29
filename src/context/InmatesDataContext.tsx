import { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';

export interface Inmate {
  id: string;
  lastName: string;
  firstName: string;
  dob: string;
  age: number;
  sex: string;
  raceSexAge: string;
  location: string;
  cellule?: string;
  arrested: string;
  sentenceDuration: string;
  released: string;
  releaseStatus: 'safe' | 'warning' | 'danger';
  photoUrl?: string;
  sanctions?: { date: string; description: string }[];
}

interface InmatesDataContextType {
  inmates: Inmate[];
  setInmates: (inmates: Inmate[]) => void;
  updateInmatePhoto: (id: string, photoUrl: string) => void;
}

const defaultInmates: Inmate[] = [
  { 
    id: '1', 
    lastName: 'CARTER', 
    firstName: 'Jason Aaron',
    dob: '02/14/1996',
    age: 30,
    sex: 'Male',
    raceSexAge: 'W/M/30 years',
    location: 'Judsonia, AR',
    cellule: 'A-101',
    arrested: '7/18/2026 12:45AM',
    sentenceDuration: '5 Years',
    released: '12/10/2031',
    releaseStatus: 'safe',
    sanctions: [{ date: '01/05/2026', description: 'Altération mineure' }]
  },
  { 
    id: '2', 
    lastName: 'Armstrong', 
    firstName: 'Chon Elex',
    dob: '05/20/1995',
    age: 31,
    sex: 'Male',
    raceSexAge: 'W/M/31 years',
    location: 'Pocahontas, AR',
    cellule: 'B-205',
    arrested: '7/18/2026 10:20AM',
    sentenceDuration: '1 Year',
    released: '08/15/2026',
    releaseStatus: 'warning',
    sanctions: []
  },
  { 
    id: '3', 
    lastName: 'Duren', 
    firstName: 'Christopher',
    dob: '11/04/1994',
    age: 32,
    sex: 'Male',
    raceSexAge: 'W/M/32 years',
    location: 'Searcy, AR',
    cellule: 'A-102',
    arrested: '7/18/2026 12:00AM',
    sentenceDuration: '6 Months',
    released: '05/01/2026',
    releaseStatus: 'danger',
    sanctions: [{ date: '03/12/2026', description: 'Bagarre dans la cour' }]
  },
  { 
    id: '4', 
    lastName: 'Valencia', 
    firstName: 'Jose M',
    dob: '01/30/1986',
    age: 40,
    sex: 'Male',
    raceSexAge: 'U/M/40 years',
    location: 'Bradford, AR',
    cellule: 'C-080',
    arrested: '7/18/2026 12:34AM',
    sentenceDuration: 'Life',
    released: 'Not Released',
    releaseStatus: 'safe',
    sanctions: []
  }
];

const InmatesDataContext = createContext<InmatesDataContextType | undefined>(undefined);

export const InmatesDataProvider = ({ children }: { children: ReactNode }) => {
  const [inmates, setInmates] = useState<Inmate[]>(defaultInmates);

  const updateInmatePhoto = (id: string, photoUrl: string) => {
    setInmates(prev => prev.map(inmate => 
      inmate.id === id ? { ...inmate, photoUrl } : inmate
    ));
  };

  return (
    <InmatesDataContext.Provider value={{ inmates, setInmates, updateInmatePhoto }}>
      {children}
    </InmatesDataContext.Provider>
  );
};

export const useInmatesData = () => {
  const context = useContext(InmatesDataContext);
  if (context === undefined) {
    throw new Error("useInmatesData doit être utilisé au sein d'un InmatesDataProvider");
  }
  return context;
};
