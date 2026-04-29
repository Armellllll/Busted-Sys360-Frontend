import { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';

export type Inmate = {
  id: string;
  firstName: string;
  lastName: string;
  dob: string;
  age: number;
  sex: string;
  raceSexAge: string;
  arrested: string;
  sentenceDuration: string;
  released: string;
  releaseStatus: 'safe' | 'warning' | 'danger'; // safe=far, warning=<3mo, danger=imminent
  location: string;
};

type InmateSelectionContextType = {
  selectedInmate: Inmate | null;
  setSelectedInmate: (inmate: Inmate | null) => void;
};

const InmateSelectionContext = createContext<InmateSelectionContextType | undefined>(undefined);

export const InmateSelectionProvider = ({ children }: { children: ReactNode }) => {
  const [selectedInmate, setSelectedInmate] = useState<Inmate | null>(null);

  return (
    <InmateSelectionContext.Provider value={{ selectedInmate, setSelectedInmate }}>
      {children}
    </InmateSelectionContext.Provider>
  );
};

export const useSelectedInmate = () => {
  const context = useContext(InmateSelectionContext);
  if (context === undefined) {
    throw new Error('useSelectedInmate must be used within an InmateSelectionProvider');
  }
  return context;
};
