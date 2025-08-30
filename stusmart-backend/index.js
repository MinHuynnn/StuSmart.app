const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const bodyParser = require('body-parser');

const app = express();
app.use(cors());
app.use(bodyParser.json());

// Kết nối MongoDB
const uri = 'mongodb+srv://Minhuy_nnn:huy012230@stusmart.wp8c8q8.mongodb.net/StuSmart?retryWrites=true&w=majority';
mongoose.connect(uri, { useNewUrlParser: true, useUnifiedTopology: true })
    .then(() => console.log('MongoDB connected!'))
    .catch(err => console.log(err));
// Thiết lập port cho server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
// Định nghĩa schema và model cho Counter (bộ đếm tự tăng)
const counterSchema = new mongoose.Schema({
    _id: String,
    seq: Number
});
const Counter = mongoose.model('Counter', counterSchema);

// Hàm lấy id tự tăng
async function getNextSequence(name) {
    const ret = await Counter.findByIdAndUpdate(
        name,
        { $inc: { seq: 1 } },
        { new: true, upsert: true }
    );
    return ret.seq;
}

// ===== MODEL KHAI BÁO TRƯỚC =====
// Student
const studentSchema = new mongoose.Schema({
    id: Number, // id tự tăng
    username: String,
    password: String,
    className: String,
    fullName: String,
    email: String,
    birthDate: String,
    parentName: String,
    parentPhone: String,
    address: String
});
const Student = mongoose.model('Student', studentSchema);

// ==== STUDENT API ====
app.post('/api/students/login', async (req, res) => {
    console.log("Login body:", req.body);
    const { username, password } = req.body;
    try {
        const student = await Student.findOne({ username, password });
        if (!student) return res.status(401).json({ error: 'Sai tài khoản hoặc mật khẩu' });
        res.json(student);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});
app.post('/api/students', async (req, res) => {
    console.log("AddStudent body:", req.body);
    try {
        const nextId = await getNextSequence('studentid');
        const student = new Student({ ...req.body, id: nextId });
        await student.save();
        res.status(201).json(student);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});
app.get('/api/students', async (req, res) => {
    try {
        const students = await Student.find();
        res.json(students);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});
app.get('/api/students/:id', async (req, res) => {
    try {
        const student = await Student.findById(req.params.id);
        if (!student) return res.status(404).json({ error: 'Student not found' });
        res.json(student);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});
app.put('/api/students/:id', async (req, res) => {
    try {
        const student = await Student.findByIdAndUpdate(
            req.params.id,
            req.body,
            { new: true }
        );
        if (!student) return res.status(404).json({ error: 'Student not found' });
        res.json(student);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});
app.delete('/api/students/:id', async (req, res) => {
    try {
        const student = await Student.findByIdAndDelete(req.params.id);
        if (!student) return res.status(404).json({ error: 'Student not found' });
        res.json({ message: 'Student deleted' });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Teacher
const teacherSchema = new mongoose.Schema({
    id: Number, // id tự tăng
    firstName: String,
    lastName: String,
    idCard: String,
    gmail: String,
    phone: String,
    password: String
});
const Teacher = mongoose.model('Teacher', teacherSchema);

// ==== TEACHER API ====
app.post('/api/teachers/login', async (req, res) => {
    const { gmail, password } = req.body;
    try {
        const teacher = await Teacher.findOne({ gmail, password });
        if (!teacher) return res.status(401).json({ error: 'Sai tài khoản hoặc mật khẩu' });
        res.json(teacher);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});
app.post('/api/teachers', async (req, res) => {
    try {
        const nextId = await getNextSequence('teacherid');
        const teacher = new Teacher({ ...req.body, id: nextId });
        await teacher.save();
        res.status(201).json(teacher);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});
app.get('/api/teachers', async (req, res) => {
    try {
        const teachers = await Teacher.find();
        res.json(teachers);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});
app.get('/api/teachers/:id', async (req, res) => {
    try {
        const teacher = await Teacher.findById(req.params.id);
        if (!teacher) return res.status(404).json({ error: 'Teacher not found' });
        res.json(teacher);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});
app.put('/api/teachers/:id', async (req, res) => {
    try {
        const teacher = await Teacher.findByIdAndUpdate(
            req.params.id,
            req.body,
            { new: true }
        );
        if (!teacher) return res.status(404).json({ error: 'Teacher not found' });
        res.json(teacher);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});
app.delete('/api/teachers/:id', async (req, res) => {
    try {
        const teacher = await Teacher.findByIdAndDelete(req.params.id);
        if (!teacher) return res.status(404).json({ error: 'Teacher not found' });
        res.json({ message: 'Teacher deleted' });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Attendance
const AttendanceSchema = new mongoose.Schema({
  studentUsername: String,
  className: String,
  date: String,
  isPresent: Boolean,
  isAbsent: Boolean
});
const Attendance = mongoose.model('Attendance', AttendanceSchema);

// ==== ATTENDANCE API ====
app.post('/attendance', async (req, res) => {
  console.log('Received attendance:', JSON.stringify(req.body, null, 2));
  try {
    await Attendance.insertMany(req.body);
    console.log('Lưu điểm danh thành công!');
    res.status(201).json({ message: 'Lưu điểm danh thành công!' });
  } catch (err) {
    console.error('Lỗi lưu điểm danh:', err);
    res.status(500).json({ error: 'Lỗi server' });
  }
});



