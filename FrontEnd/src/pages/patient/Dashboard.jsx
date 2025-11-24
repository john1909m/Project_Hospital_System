import { useEffect, useState } from "react";
import { Calendar, FileText, Activity, Heart, Plus } from "lucide-react";
import { DashboardCard } from "@/components/DashboardCard";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useToast } from "@/hooks/use-toast";
import "./dashboard.css"

export default function PatientDashboard() {
  const [user, setUser] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [prescriptions, setPrescriptions] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedDoctor, setSelectedDoctor] = useState("");
  const [appointmentDate, setAppointmentDate] = useState("");

  const { toast } = useToast();
  const token = localStorage.getItem("token");

  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser) setUser(JSON.parse(storedUser));
  }, []);

  useEffect(() => {
    if (!user) return;

    const fetchAppointments = async () => {
      try {
        const res = await fetch(
          `http://localhost:8080/appointment/patient/${user.patient.patientId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        if (!res.ok) throw new Error("Failed to load appointments");
        const data = await res.json();
        setAppointments(data);
      } catch (err) {
        console.error(err);
      }
    };

    const fetchPrescriptions = async () => {
      try {
        const res = await fetch(
          `http://localhost:8080/prescriptions/patient/${user.patient.patientId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        if (!res.ok) throw new Error("Failed to load prescriptions");
        const data = await res.json();
        setPrescriptions(data);
      } catch (err) {
        console.error(err);
      }
    };

    const fetchDoctors = async () => {
      try {
        const res = await fetch("http://localhost:8080/doctors", {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (!res.ok) throw new Error("Failed to load doctors");
        const data = await res.json();
        setDoctors(data);
      } catch (err) {
        console.error(err);
      }
    };

    Promise.all([fetchAppointments(), fetchPrescriptions(), fetchDoctors()]).finally(() =>
      setLoading(false)
    );
  }, [user, token]);

  const handleAddAppointment = async (e) => {
    e.preventDefault();
    if (!selectedDoctor || !appointmentDate) return;

    try {
      const doctor = doctors.find((d) => d.doctorId.toString() === selectedDoctor);
      const payload = {
        patientName: user.patient.patientName,
        patientId: user.patient.patientId,
        doctorName: doctor.doctorName,
        doctorId: doctor.doctorId,
        appointmentDate,
        status: "active",
      };

      const res = await fetch("http://localhost:8080/appointment/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (!res.ok) throw new Error("Failed to add appointment");
      const newAppointment = await res.json();

      // Show success toast
      toast({ title: "Appointment added successfully!" });

      // Close modal
      setModalOpen(false);

      // Reset form
      setSelectedDoctor("");
      setAppointmentDate("");

      // Immediately add the new appointment to the list
      setAppointments((prev) => [...prev, newAppointment]);
    } catch (err) {
      console.error(err);
      toast({ title: "Failed to add appointment", variant: "destructive" });
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between transition-base">
        <div>
          <h1 className="text-3xl font-bold tracking-tight mb-2">
            Welcome, {user ? user.patient?.patientName : "Loading..."}
          </h1>
          <p className="text-muted-foreground text-base leading-relaxed">
            Your health dashboard and upcoming appointments.
          </p>
        </div>
        <Button onClick={() => setModalOpen(true)} className="flex items-center gap-2">
          <Plus className="w-4 h-4" /> Add Appointment
        </Button>
      </div>

      {/* Upcoming Appointments */}
      <div className="grid gap-4 md:grid-cols-2">
        <Card className="transition-base hover:shadow-sm">
          <CardHeader>
            <CardTitle>Upcoming Appointments</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {loading ? (
              <p>Loading...</p>
            ) : appointments.length > 0 ? (
              appointments.map((appointment) => (
                <div
                  key={appointment.appointmentId}
                  className="flex items-center justify-between p-3 rounded-lg border transition-base hover:bg-muted/30"
                >
                  <div>
                    <p className="font-medium">Dr: {appointment.doctorName}</p>
                    <p className="text-sm text-muted-foreground">{appointment.status}</p>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-medium">{appointment.appointmentDate}</p>
                  </div>
                </div>
              ))
            ) : (
              <p>No appointments found</p>
            )}
          </CardContent>
        </Card>

        {/* Recent Prescriptions */}
        <Card className="transition-base hover:shadow-sm">
          <CardHeader>
            <CardTitle>Recent Prescriptions</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {loading ? (
              <p>Loading prescriptions...</p>
            ) : prescriptions.length > 0 ? (
              prescriptions.map((prescription) => (
                <div
                  key={prescription.prescriptionId}
                  className="p-3 rounded-lg border transition-base hover:bg-muted/30 space-y-2"
                >
                  <div className="flex items-center justify-between">
                    <p className="font-medium">Dr. {prescription.doctorName}</p>
                    <p className="text-sm font-medium">{prescription.dateIssued}</p>
                  </div>
                  <p className="text-sm text-muted-foreground">Notes: {prescription.notes}</p>
                  <div className="text-sm">
                    <p className="font-medium">Medicines:</p>
                    <ul className="list-disc list-inside text-muted-foreground">
                      {prescription.medicineNames?.map((med, index) => (
                        <li key={index}>{med}</li>
                      ))}
                    </ul>
                  </div>
                </div>
              ))
            ) : (
              <p>No prescriptions found</p>
            )}
          </CardContent>
        </Card>
      </div>

      {/* Add Appointment Modal */}
      {modalOpen && (
        <div className="fixed inset-0 bg-black back bg-opacity-30 flex items-center justify-center z-50">
          <div className="bg-background modal p-6 rounded-lg max-w-md shadow-lg space-y-4">
            <h2 className="text-xl font-bold">Add Appointment</h2>
            <form onSubmit={handleAddAppointment} className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="appointmentDate">Appointment Date</Label>
                <Input
                  id="appointmentDate"
                  type="datetime-local"
                  value={appointmentDate}
                  onChange={(e) => setAppointmentDate(e.target.value)}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="doctor">Select Doctor</Label>
                <select
                  id="doctor"
                  value={selectedDoctor}
                  onChange={(e) => setSelectedDoctor(e.target.value)}
                  className="w-full border rounded px-2 py-1"
                  required
                >
                  <option value="">Select a doctor</option>
                  {doctors.map((doc) => (
                    <option key={doc.doctorId} value={doc.doctorId}>
                      {doc.doctorName}
                    </option>
                  ))}
                </select>
              </div>
              <div className="flex justify-end gap-2">
                <Button type="button" variant="outline" onClick={() => setModalOpen(false)}>
                  Cancel
                </Button>
                <Button type="submit">Add</Button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

// {
  // "usernme"="john",
  // password="156"
// }
