import connectMongoDB from "@/app/libs/mongodb";
import Topic from "@/app/module/topic";
import { NextResponse } from "next/server";

export async function POST(request) {
    const { title, description } = await request.json();
    await connectMongoDB();
    await Topic.create({title, description});

    return NextResponse.json({ message: "Topic created"}, {status: 201 })
}

export async function GET() {
    await connectMongoDB();
    const topics = await Topic.find();

    return NextResponse.json({topics})
}

/** http://localhost:3000/api/topics?id=6652806c937f412a6b0e1d65 */
export async function DELETE(request) {
    const id = request.nextUrl.searchParams.get("id");

    await connectMongoDB();
    await Topic.findByIdAndDelete(id);
    return NextResponse.json({message: "Topic deleted"}, {status: 200});
}

// export const dynamic = "force-dynamic";