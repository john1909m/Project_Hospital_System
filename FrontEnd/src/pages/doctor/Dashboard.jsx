import { useEffect, useState } from "react";
import { Calendar, Users, FileText, Clock, Plus } from "lucide-react";
import { DashboardCard } from "@/components/DashboardCard";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { useToast } from "@/hooks/use-toast";
import "../patient/dashboard.css"

export default function DoctorDashboard() {
  const [user, setUser] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [patients, setPatients] = useState([]);
  const [medicines, setMedicines] = useState([]);

  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);

  // form fields
  const [selectedPatient, setSelectedPatient] = useState("");
  const [selectedMedicines, setSelectedMedicines] = useState([]);
  const [date, setDate] = useState("");
  const [notes, setNotes] = useState("");

  const { toast } = useToast();
  const token = localStorage.getItem("token");

  useEffect(() => {
    const u = localStorage.getItem("user");
    if (u) setUser(JSON.parse(u));
  }, []);

  useEffect(() => {
    if (!user) return;

    const doctorId = user.doctor?.doctorId;

    const fetchAppointments = async () => {
      const res = await fetch(
        `http://localhost:8080/${doctorId}/appointments`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setAppointments(await res.json());
    };

    const fetchPatients = async () => {
      const res = await fetch(
        `http://localhost:8080/${doctorId}/patients`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setPatients(await res.json());
    };

    const fetchMedicines = async () => {
      const res = await fetch(`http://localhost:8080/medicines`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setMedicines(await res.json());
    };

    Promise.all([fetchAppointments(), fetchPatients(), fetchMedicines()]).finally(() =>
      setLoading(false)
    );
  }, [user, token]);

  const handleAddPrescription = async (e) => {
    e.preventDefault();
    if (!selectedPatient || selectedMedicines.length === 0 || !date) return;

    const patient = patients.find((p) => p.patientId.toString() === selectedPatient);

    const payload = {
      doctorId: user.doctor.doctorId,
      doctorName: user.doctor.doctorName,
      patientId: patient.patientId,
      patientName: patient.patientName,
      notes,
      date,
      medicineIds: selectedMedicines.map((m) => Number(m)),
      medicineNames: selectedMedicines.map(
        (id) => medicines.find((m) => m.medicineId.toString() === id)?.medicineName
      ),
    };

    try {
      const res = await fetch("http://localhost:8080/prescription/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (!res.ok) throw new Error("Failed to add");

      toast({ title: "Prescription Added Successfully!" });

      setModalOpen(false);
      setSelectedPatient("");
      setSelectedMedicines([]);
      setNotes("");
      setDate("");
    } catch (err) {
      toast({ title: "Error adding prescription", variant: "destructive" });
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">
          Welcome, Dr. {user?.doctor?.doctorName}
        </h1>

        {/* Add Prescription Button */}
        <Button onClick={() => setModalOpen(true)} className="flex items-center gap-2">
          <Plus className="w-4 h-4" /> Add Prescription
        </Button>
      </div>

      {/* Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <DashboardCard
          title="Appointments"
          value={appointments.length}
          icon={Calendar}
          description="Total doctor appointments"
          variant="primary"
        />
        <DashboardCard
          title="Patients"
          value={patients.length}
          icon={Users}
          description="Assigned to you"
          variant="success"
        />
      </div>

      {/* Appointments List */}
      <Card>
        <CardHeader>
          <CardTitle>Doctor Appointments</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {appointments.length > 0 ? (
            appointments.map((app) => (
              <div
                key={app.appointmentId}
                className="flex items-center justify-between p-3 rounded-lg border hover:bg-muted/30"
              >
                <p className="font-medium">{app.patientName}</p>
                <p>{app.status}</p>
                <p className="text-sm">{app.appointmentDate}</p>
              </div>
            ))
          ) : (
            <p>No appointments</p>
          )}
        </CardContent>
      </Card>

      {/* Patients List */}
      <Card>
        <CardHeader>
          <CardTitle>Your Patients</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {patients.length > 0 ? (
            patients.map((p) => (
              <div
                key={p.patientId}
                className="p-3 rounded-lg border hover:bg-muted/30 flex justify-between"
              >
                <p className="font-medium">{p.patientName}</p>
                <p className="text-sm text-muted-foreground">{p.patientPhone}</p>
              </div>
            ))
          ) : (
            <p>No patients found</p>
          )}
        </CardContent>
      </Card>

      {/* Add Prescription Modal */}
      {modalOpen && (
        <div className="fixed inset-0 back bg-black/40 flex items-center justify-center z-50">
          <div className="bg-background modal p-6 rounded-lg max-w-md space-y-4 shadow-lg">
            <h2 className="text-xl font-bold mb-2">Add Prescription</h2>

            <form onSubmit={handleAddPrescription} className="space-y-4">

              {/* Date */}
              <div className="space-y-1">
                <Label>Date</Label>
                <Input type="date" value={date} onChange={(e) => setDate(e.target.value)} required />
              </div>

              {/* Notes */}
              <div className="space-y-1">
                <Label>Notes</Label>
                <Input
                  type="text"
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
                  placeholder="Enter notes"
                  required
                />
              </div>

              {/* Select Patient */}
              <div className="space-y-1">
                <Label>Patient</Label>
                <select
                  className="w-full border rounded px-2 py-1"
                  value={selectedPatient}
                  onChange={(e) => setSelectedPatient(e.target.value)}
                  required
                >
                  <option value="">Select patient</option>
                  {patients.map((p) => (
                    <option key={p.patientId} value={p.patientId}>
                      {p.patientName}
                    </option>
                  ))}
                </select>
              </div>

              {/* Multi Select Medicines */}
              <div className="space-y-1">
                <Label>Medicines</Label>
                <select
                  className="w-full border rounded px-2 py-1"
                  multiple
                  value={selectedMedicines}
                  onChange={(e) =>
                    setSelectedMedicines(Array.from(e.target.selectedOptions, (opt) => opt.value))
                  }
                  required
                >
                  {medicines.map((m) => (
                    <option key={m.medicineId} value={m.medicineId}>
                      {m.medicineName}
                    </option>
                  ))}
                </select>
                <p className="text-xs text-muted-foreground">Hold CTRL to select multiple</p>
              </div>

              {/* Buttons */}
              <div className="flex justify-end gap-2">
                <Button variant="outline" onClick={() => setModalOpen(false)} type="button">
                  Cancel
                </Button>
                <Button type="submit">Add Prescription</Button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
