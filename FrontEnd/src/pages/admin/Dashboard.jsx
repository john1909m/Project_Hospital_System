// AdminDashboard.jsx
import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/components/ui/card";
import { useToast } from "@/hooks/use-toast";
import {
  Plus,
  Users,
  User,
  Calendar,
  FileText,
  Pill,
  Loader2,
  X,
} from "lucide-react";
import { Badge } from "@/components/ui/badge";
import "./dashboard.css";

export default function AdminDashboard() {
  const [doctors, setDoctors] = useState([]);
  const [patients, setPatients] = useState([]);
  const [appointments, setAppointments] = useState([]);
  const [prescriptions, setPrescriptions] = useState([]);
  const [medicines, setMedicines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [modalType, setModalType] = useState("");
  const [formFields, setFormFields] = useState({});
  const [submitting, setSubmitting] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const { toast } = useToast();
  const token = localStorage.getItem("token") || "";

  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]);

  const fetchData = async () => {
    try {
      setLoading(true);
      const headers = { Authorization: `Bearer ${token}`, "Content-Type": "application/json" };

      const [
        doctorsRes,
        patientsRes,
        appointmentsRes,
        prescriptionsRes,
        medicinesRes,
      ] = await Promise.all([
        fetch("http://localhost:8080/doctors", { headers }),
        fetch("http://localhost:8080/patients", { headers }),
        fetch("http://localhost:8080/appointments", { headers }),
        fetch("http://localhost:8080/prescriptions", { headers }),
        fetch("http://localhost:8080/medicines", { headers }),
      ]);

      if (!doctorsRes.ok) throw new Error("Failed to load doctors");
      if (!patientsRes.ok) throw new Error("Failed to load patients");
      if (!appointmentsRes.ok) throw new Error("Failed to load appointments");
      if (!prescriptionsRes.ok) throw new Error("Failed to load prescriptions");
      if (!medicinesRes.ok) throw new Error("Failed to load medicines");

      setDoctors(await doctorsRes.json());
      setPatients(await patientsRes.json());
      setAppointments(await appointmentsRes.json());
      setPrescriptions(await prescriptionsRes.json());
      setMedicines(await medicinesRes.json());
    } catch (err) {
      console.error(err);
      toast({ title: "Failed to load data", variant: "destructive" });
    } finally {
      setLoading(false);
    }
  };

  const openModal = (type, item = null) => {
    setModalType(type);
    if (item) {
      // Edit mode
      const fields = getFormFieldsFromItem(type, item);
      setFormFields(fields);
      setIsEditMode(true);
    } else {
      // Add mode
      setFormFields(getDefaultFields(type));
      setIsEditMode(false);
    }
    setModalOpen(true);
  };

  const getDefaultFields = (type) => {
    const defaults = {
      appointment: { status: "scheduled", patientId: "", doctorId: "", appointmentDate: new Date().toISOString() },
      prescription: { patientId: "", doctorId: "", medicines: [], medicineIds: [], medicineNames: [], notes: "" },
      medicine: { medicineName: "", medicinePrice: "0.00", medicineDescription: "", medicineCategory: "" },
      doctor: {
        username: "",
        email: "",
        password: "",
        doctorName: "",
        doctorPhone: "",
        doctorDepartment: "",
      },
      patient: {
        username: "",
        email: "",
        password: "",
        patientName: "",
        patientPhone: "",
        patientGender: "male",
        patientAge: 0,
      },
    };
    return defaults[type] || {};
  };

  const getFormFieldsFromItem = (type, item) => {
    if (type === "doctor") {
      return {
        doctorId: item.doctorId,
        doctorName: item.doctorName,
        doctorPhone: item.doctorPhone,
        doctorDepartment: item.doctorDepartment,
      };
    } else if (type === "patient") {
      return {
        patientId: item.patientId,
        patientName: item.patientName,
        patientPhone: item.patientPhone,
        patientGender: item.patientGender,
        patientAge: item.patientAge,
      };
    } else if (type === "appointment") {
      return {
        appointmentId: item.appointmentId,
        patientId: item.patientId || item.patient?.patientId,
        doctorId: item.doctorId || item.doctor?.doctorId,
        appointmentDate: item.appointmentDate,
        status: item.status,
      };
    } else if (type === "prescription") {
      return {
        prescriptionId: item.prescriptionId,
        patientId: item.patientId || item.patient?.patientId,
        doctorId: item.doctorId || item.doctor?.doctorId,
        medicineIds: item.medicineIds || [],
        medicineNames: item.medicineNames || [],
        notes: item.notes,
      };
    } else if (type === "medicine") {
      return {
        medicineId: item.medicineId,
        medicineName: item.medicineName,
        medicineDescription: item.medicineDescription,
        medicinePrice: item.medicinePrice,
        medicineCategory: item.medicineCategory,
      };
    }
    return {};
  };

  const closeModal = () => {
    setModalOpen(false);
    setFormFields({});
    setSubmitting(false);
    setIsEditMode(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);

    try {
      const configs = {
        doctor: { url: "http://localhost:8080/admin/add/user", method: "POST" },
        patient: { url: "http://localhost:8080/admin/add/user", method: "POST" },
        appointment: { url: "http://localhost:8080/appointment/add", method: "POST" },
        prescription: { url: "http://localhost:8080/prescription/add", method: "POST" },
        medicine: { url: "http://localhost:8080/medicine/add", method: "POST" },
      };

      let cfg = configs[modalType];
      if (!cfg) throw new Error("Unknown modal type");

      // Update URLs if editing
      if (isEditMode) {
        if (modalType === "doctor") cfg = { url: "http://localhost:8080/doctor/update", method: "PUT" };
        if (modalType === "patient") cfg = { url: "http://localhost:8080/patient/update", method: "PUT" };
        if (modalType === "appointment") cfg = { url: "http://localhost:8080/appointment/update", method: "PUT" };
        if (modalType === "prescription") cfg = { url: "http://localhost:8080/prescription/update", method: "PUT" };
        if (modalType === "medicine") cfg = { url: "http://localhost:8080/medicine/update", method: "PUT" };
      }

      let body = { ...formFields };

      // Doctor
      if (modalType === "doctor" && !isEditMode) {
        body = {
          username: formFields.username.trim(),
          email: formFields.email.trim(),
          password: formFields.password,
          doctor: {
            doctorName: formFields.doctorName.trim(),
            doctorPhone: formFields.doctorPhone.trim(),
            doctorDepartment: formFields.doctorDepartment.trim(),
          },
          roles: [{ roleId: 2, roleName: "DOCTOR" }],
        };
      }
      // Patient
      if (modalType === "patient" && !isEditMode) {
        body = {
          username: formFields.username.trim(),
          email: formFields.email.trim(),
          password: formFields.password,
          patient: {
            patientName: formFields.patientName.trim(),
            patientPhone: formFields.patientPhone.trim(),
            patientGender: formFields.patientGender,
            patientAge: Number(formFields.patientAge),
          },
          roles: [{ roleId: 3, roleName: "PATIENT" }],
        };
      }

      const res = await fetch(cfg.url, {
        method: cfg.method,
        headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
        credentials: "include",
        body: JSON.stringify(body),
      });

      if (!res.ok) {
        const text = await res.text().catch(() => null);
        console.error("Server error:", res.status, text);
        throw new Error("Request failed");
      }

      toast({ title: `${modalType.charAt(0).toUpperCase() + modalType.slice(1)} saved successfully!` });
      await fetchData();
      closeModal();
    } catch (err) {
      console.error(err);
      toast({ title: "Operation failed", variant: "destructive" });
    } finally {
      setSubmitting(false);
    }
  };

  const getIcon = (type) => {
    const icons = { doctor: Users, patient: User, appointment: Calendar, prescription: FileText, medicine: Pill };
    return icons[type] || Users;
  };

  const getCardData = (type) => {
    const data = { doctor: doctors, patient: patients, appointment: appointments, prescription: prescriptions, medicine: medicines };
    return data[type] || [];
  };

  const handleEdit = (item, type) => {
    openModal(type, item);
  };

  const DataRow = ({ item, type }) => {
    const getDisplayText = () => {
      switch (type) {
        case "doctor": return `${item.doctorName || item.username || "—"} - ${item.doctorDepartment || ""}`;
        case "patient": return `${item.patientName || item.username || "—"} (${item.patientAge || 0})`;
        case "appointment": return `${item.patientName || item.patient?.patientName || "—"} - ${formatDate(item.appointmentDate)}`;
        case "prescription": return `${item.patientName || item.patient?.patientName || "—"} - ${item.doctor?.doctorName || "—"}`;
        case "medicine": return `${item.medicineName || item.name || "—"} - $${item.medicinePrice || "0.00"}`;
        default: return item.name || "Unknown";
      }
    };

    const getStatusBadge = () => {
      if (type === "appointment" && item.status) {
        const statusColors = { scheduled: "bg-blue-100 text-blue-800", active: "bg-blue-100 text-blue-800", completed: "bg-green-100 text-green-800", cancelled: "bg-red-100 text-red-800" };
        return <Badge className={statusColors[item.status] || "bg-gray-100"}>{item.status}</Badge>;
      }
      return null;
    };

    return (
      <div className="flex items-center justify-between p-3 rounded-lg border bg-card hover:bg-accent/50 transition-colors">
        <div className="flex flex-col gap-1">
          <p className="text-sm font-medium truncate">{getDisplayText()}</p>
          {getStatusBadge()}
        </div>
        <div className="flex gap-2">
          <Button size="sm" onClick={() => handleEdit(item, type)}>Edit</Button>
          {/* Only show delete for appointment/prescription/medicine */}
          {["appointment", "prescription", "medicine"].includes(type) && (
            <Button size="sm" variant="destructive" onClick={() => handleDelete(item, type)}>Delete</Button>
          )}
        </div>
      </div>
    );
  };

  const handleDelete = async (item, type) => {
    if (!confirm(`Are you sure you want to delete this ${type}?`)) return;

    let url = "";
    if (type === "appointment") url = `http://localhost:8080/appointment/delete/${item.appointmentId}`;
    if (type === "prescription") url = `http://localhost:8080/prescription/delete/${item.prescriptionId}`;
    if (type === "medicine") url = `http://localhost:8080/medicine/delete/${item.medicineId}`;

    try {
      const res = await fetch(url, { method: "DELETE", headers: { Authorization: `Bearer ${token}` } });
      if (!res.ok) throw new Error("Delete failed");
      toast({ title: `${type.charAt(0).toUpperCase() + type.slice(1)} deleted!` });
      await fetchData();
    } catch (err) {
      console.error(err);
      toast({ title: "Delete failed", variant: "destructive" });
    }
  };

  const DataCard = ({ type, title }) => {
    const Icon = getIcon(type);
    const data = getCardData(type);

    return (
      <Card className="hover:shadow-lg transition-shadow duration-300">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-4">
          <div className="space-y-1">
            <CardTitle className="text-xl flex items-center gap-2"><Icon className="h-5 w-5" />{title}</CardTitle>
            <CardDescription>{data.length} total</CardDescription>
          </div>
          <Button onClick={() => openModal(type)} size="sm" className="gap-1">
            <Plus className="h-4 w-4" />Add
          </Button>
        </CardHeader>
        <CardContent className="max-h-64 overflow-y-auto">
          <div className="space-y-2">
            {data.length > 0 ? data.map((item, idx) => <DataRow key={idx} item={item} type={type} />) : (
              <div className="text-center text-muted-foreground py-4">No {type}s found</div>
            )}
          </div>
        </CardContent>
      </Card>
    );
  };

  function formatDate(dateStr) {
    if (!dateStr) return "—";
    try { const d = new Date(dateStr); return d.toLocaleString(); } catch { return dateStr; }
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <Loader2 className="h-8 w-8 animate-spin mx-auto mb-4" />
          <p className="text-lg">Loading dashboard...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50 p-6">
      <div className="max-w-7xl mx-auto space-y-6">
        <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center gap-4">
          <div>
            <h1 className="text-4xl font-bold text-gray-900">Admin Dashboard</h1>
            <p className="text-gray-600 mt-2">Manage your healthcare system efficiently</p>
          </div>
        </div>

        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5">
          <DataCard type="doctor" title="Doctors" />
          <DataCard type="patient" title="Patients" />
          <DataCard type="appointment" title="Appointments" />
          <DataCard type="prescription" title="Prescriptions" />
          <DataCard type="medicine" title="Medicines" />
        </div>

        {modalOpen && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
            <div className="absolute back inset-0 bg-white/30 backdrop-blur-sm"></div>
            <div className="relative modal w-fit rounded-xl max-w-md max-h-[80vh] overflow-y-auto shadow-2xl bg-white">
              <div className="flex items-center bg-white justify-between p-6 border-b">
                <h2 className="text-xl font-semibold capitalize">{isEditMode ? "Edit" : "Add"} {modalType}</h2>
                <Button variant="ghost" size="icon" onClick={closeModal}><X className="h-4 w-4" /></Button>
              </div>

              <form onSubmit={handleSubmit} className="p-6 space-y-4">
                {/* Doctor Form */}
                {modalType === "doctor" && (
                  <>
                    {!isEditMode && (
                      <>
                        <div className="flex flex-col">
                          <label htmlFor="username">Username</label>
                          <input type="text" name="username" className="input_add"
                            value={formFields.username || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                        </div>
                        <div className="flex flex-col">
                          <label htmlFor="email">Email</label>
                          <input type="email" name="email" className="input_add"
                            value={formFields.email || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                        </div>
                        <div className="flex flex-col">
                          <label htmlFor="password">Password</label>
                          <input type="password" name="password" className="input_add"
                            value={formFields.password || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                        </div>
                      </>
                    )}

                    <div className="flex flex-col">
                      <label htmlFor="doctorName">Doctor Name</label>
                      <input type="text" name="doctorName" className="input_add"
                        value={formFields.doctorName || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                    </div>
                    <div className="flex flex-col">
                      <label htmlFor="doctorPhone">Phone</label>
                      <input type="text" name="doctorPhone" className="input_add"
                        value={formFields.doctorPhone || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                    </div>
                    <div className="flex flex-col">
                      <label htmlFor="doctorDepartment">Department</label>
                      <input type="text" name="doctorDepartment" className="input_add"
                        value={formFields.doctorDepartment || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                    </div>
                  </>
                )}

                {/* Patient Form */}
                {modalType === "patient" && (
                  <>
                    {!isEditMode && (
                      <>
                        <div className="flex flex-col">
                          <label htmlFor="username">Username</label>
                          <input type="text" name="username" className="input_add"
                            value={formFields.username || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                        </div>
                        <div className="flex flex-col">
                          <label htmlFor="email">Email</label>
                          <input type="email" name="email" className="input_add"
                            value={formFields.email || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                        </div>
                        <div className="flex flex-col">
                          <label htmlFor="password">Password</label>
                          <input type="password" name="password" className="input_add"
                            value={formFields.password || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                        </div>
                      </>
                    )}

                    <div className="flex flex-col">
                      <label htmlFor="patientName">Patient Name</label>
                      <input type="text" name="patientName" className="input_add"
                        value={formFields.patientName || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                    </div>
                    <div className="flex flex-col">
                      <label htmlFor="patientPhone">Phone</label>
                      <input type="text" name="patientPhone" className="input_add"
                        value={formFields.patientPhone || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                    </div>
                    <div className="flex flex-col">
                      <label htmlFor="patientGender">Gender</label>
                      <select name="patientGender" className="input_add"
                        value={formFields.patientGender || ""} onChange={(e)=>setFormFields({...formFields,patientGender:e.target.value})}>
                        <option value="">Select Gender</option>
                        <option value="male">Male</option>
                        <option value="female">Female</option>
                        <option value="other">Other</option>
                      </select>
                    </div>
                    <div className="flex flex-col">
                      <label htmlFor="patientAge">Age</label>
                      <input type="number" name="patientAge" className="input_add"
                        value={formFields.patientAge || ""} onChange={(e)=>setFormFields({...formFields,[e.target.name]:e.target.value})} />
                    </div>
                  </>
                )}

                {/* Appointment Form */}
                {modalType === "appointment" && (
                  <>

                   {/* Patient Select */}
                      <div className="flex flex-col">
                        <label htmlFor="patientId">Patient</label>
                        <select
                          name="patientName"
                          className="input_add"
                          value={formFields.patientName || ""}
                          onChange={(e) =>
                            setFormFields({ ...formFields, patientName: e.target.value })
                          }
                        >
                          <option value="">Select Patient</option>
                          {patients.map((p) => (
                            <option key={p.patientName} value={p.patientName}>
                              {p.patientName || p.username}
                            </option>
                          ))}
                        </select>
                      </div>

                      {/* Doctor Select */}
                      <div className="flex flex-col">
                        <label htmlFor="doctorId">Doctor</label>
                        <select
                          name="doctorName"
                          className="input_add"
                          value={formFields.doctorName || ""}
                          onChange={(e) =>
                            setFormFields({ ...formFields, doctorName: e.target.value })
                          }
                        >
                          <option value="">Select Doctor</option>
                          {doctors.map((d) => (
                            <option key={d.doctorName} value={d.doctorName}>
                              {d.doctorName || d.username} - {d.doctorDepartment}
                            </option>
                          ))}
                        </select>
                      </div>

                    {/* Appointment Date */}
                    <div className="flex flex-col">
                      <label htmlFor="appointmentDate">Date & Time</label>
                      <input
                        className="input_add"
                        type="datetime-local"
                        name="appointmentDate"
                        value={formFields.appointmentDate || ""}
                        onChange={(e) =>
                          setFormFields({ ...formFields, [e.target.name]: e.target.value })
                        }
                      />
                    </div>

                    {/* Status */}
                    <div className="flex flex-col">
                      <label htmlFor="status">Status</label>
                      <select
                        name="status"
                        className="input_add"
                        value={formFields.status || ""}
                        onChange={(e) =>
                          setFormFields({ ...formFields, status: e.target.value })
                        }
                      >
                        <option value="">Select Status</option>
                        <option value="scheduled">Scheduled</option>
                        <option value="active">Active</option>
                        <option value="completed">Completed</option>
                        <option value="cancelled">Cancelled</option>
                      </select>
                    </div>

                  </>
                )}

                {/* Prescription Form */}
                {modalType === "prescription" && (
                  <>

                    {/* Patient Select */}
                    <div className="flex flex-col">
                      <label htmlFor="patientId">Patient</label>
                      <select
                        name="patientId"
                        className="input_add"
                        value={formFields.patientId || ""}
                        onChange={(e) =>
                          setFormFields({ ...formFields, patientId: e.target.value })
                        }
                      >
                        <option value="">Select Patient</option>
                        {patients.map((p) => (
                          <option key={p.patientId} value={p.patientId}>
                            {p.patientName || p.username}
                          </option>
                        ))}
                      </select>
                    </div>

                    {/* Doctor Select */}
                    <div className="flex flex-col">
                      <label htmlFor="doctorId">Doctor</label>
                      <select
                        name="doctorId"
                        className="input_add"
                        value={formFields.doctorId || ""}
                        onChange={(e) =>
                          setFormFields({ ...formFields, doctorId: e.target.value })
                        }
                      >
                        <option value="">Select Doctor</option>
                        {doctors.map((d) => (
                          <option key={d.doctorId} value={d.doctorId}>
                            {d.doctorName || d.username} - {d.doctorDepartment}
                          </option>
                        ))}
                      </select>
                    </div>

                    {/* Medicines Multi-Select */}
                    <div className="flex flex-col">
                      <label htmlFor="medicineIds">Medicines</label>
                      <select
                        name="medicineIds"
                        className="input_add"
                        multiple
                        value={formFields.medicineIds || []}
                        onChange={(e) => {
                          const selected = Array.from(e.target.selectedOptions, option => Number(option.value));
                          const names = Array.from(e.target.selectedOptions, option => option.text);
                          setFormFields({ ...formFields, medicineIds: selected, medicineNames: names });
                        }}
                      >
                        {medicines.map((m) => (
                          <option key={m.medicineId} value={m.medicineId}>
                            {m.medicineName}
                          </option>
                        ))}
                      </select>
                    </div>

                    {/* Notes */}
                    <div className="flex flex-col">
                      <label htmlFor="notes">Notes</label>
                      <textarea
                        name="notes"
                        className="input_add"
                        value={formFields.notes || ""}
                        onChange={(e) =>
                          setFormFields({ ...formFields, notes: e.target.value })
                        }
                        placeholder="Prescription notes"
                      />
                    </div>

                  </>
                )}

                {/* Medicine Form */}
                {modalType === "medicine" && (
                   <>
                      {/* Medicine Name */}
                      <div className="flex flex-col">
                        <label htmlFor="medicineName">Medicine Name</label>
                        <input
                          type="text"
                          name="medicineName"
                          className="input_add"
                          value={formFields.medicineName || ""}
                          onChange={(e) =>
                            setFormFields({ ...formFields, medicineName: e.target.value })
                          }
                          required
                        />
                      </div>

                      {/* Medicine Description */}
                      <div className="flex flex-col">
                        <label htmlFor="medicineDescription">Description</label>
                        <textarea
                          name="medicineDescription"
                          className="input_add"
                          value={formFields.medicineDescription || ""}
                          onChange={(e) =>
                            setFormFields({ ...formFields, medicineDescription: e.target.value })
                          }
                        />
                      </div>

                      {/* Medicine Price */}
                      <div className="flex flex-col">
                        <label htmlFor="medicinePrice">Price</label>
                        <input
                          type="number"
                          step="0.01"
                          name="medicinePrice"
                          className="input_add"
                          value={formFields.medicinePrice || ""}
                          onChange={(e) =>
                            setFormFields({ ...formFields, medicinePrice: e.target.value })
                          }
                          required
                        />
                      </div>

                      {/* Medicine Category */}
                      <div className="flex flex-col">
                        <label htmlFor="medicineCategory">Category</label>
                        <select
                          name="medicineCategory"
                          className="input_add"
                          value={formFields.medicineCategory || ""}
                          onChange={(e) =>
                            setFormFields({ ...formFields, medicineCategory: e.target.value })
                          }
                          required
                        >
                          <option value="">Select Category</option>
                          <option value="Analgesic">Analgesic</option>
                          <option value="Antibiotic">Antibiotic</option>
                          <option value="Antipyretic">Antipyretic</option>
                          <option value="Vitamin">Vitamin</option>
                          {/* add more categories if needed */}
                        </select>
                      </div>

                    </>
                )}

                <div className="flex justify-end gap-3 pt-4">
                  <Button type="button" variant="outline" onClick={closeModal}>
                    Cancel
                  </Button>
                  <Button type="submit" disabled={submitting}>
                    {submitting && <Loader2 className="h-4 w-4 animate-spin mr-2" />}
                    {isEditMode ? "Update" : "Add"} {modalType}
                  </Button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
